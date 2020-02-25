package siap.siep.misuracautelare.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.controller.SiapController;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareDAO;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.dao.MisuraCautelareBdmcDAO;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.dao.DAOException;
import f3b.util.DateUtils;
import f3b.util.F3BException;


/**
 * <p>
 * Title: MisuraCautelareController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraCautelare
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
public class MisuraCautelareController extends SiapController implements IMisuraCautelare {

	public MisuraCautelareModel ExInserisciMisuraCautelare(MisuraCautelareModel aMisuraCautelare)
			throws F3BException {

		Connection lConn = null;

		MisuraCautelareDAO lMisDao = null;
		MisuraCautelareModel lMisMod = null;

		try {
			lConn = getDBConnection();

			lMisMod = new MisuraCautelareModel(aMisuraCautelare);

			setDateMisura(aMisuraCautelare);

			lMisDao = new MisuraCautelareDAO(lConn);
			lMisDao.setDAOFromModel(aMisuraCautelare);
			BigDecimal lKey = null;
			lKey = lMisDao.insert();

			commit(lConn);

			lMisMod.setIdMisuraCautelare(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareController.ExInserisciMisuraCautelare: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	// metodo di inserimento di un vettore
	public MisuraCautelareModel ExInserisciMisuraCautelare(Vector aMisuraCautelare) throws F3BException {

		Connection lConn = null;

		MisuraCautelareDAO lMisDao = null;
		MisuraCautelareModel lMisMod = null;

		try {
			lConn = getDBTransaction();

			lMisDao = new MisuraCautelareDAO(lConn);

			for (int i = 0; i < aMisuraCautelare.size(); i++) {
				lMisMod = new MisuraCautelareModel((MisuraCautelareModel) aMisuraCautelare.get(i));

				lMisDao.setDAOFromModel(lMisMod);

				lMisDao.insert();

				lMisDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareController.ExInserisciMisuraCautelare: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	// metodo di inserimento di un vettore
	public MisuraCautelareModel ExInserisciMisuraCautelareByBdmc(Vector aMisuraCautelare, Vector aMisCautBdmc)
			throws F3BException {

		Connection lConn = null;

		MisuraCautelareDAO lMisDao = null;
		MisuraCautelareModel lMisMod = null;
		MisuraCautelareBdmcDAO lMisCautBdmcDao = null;
		MisuraCautelareBdmcModel lMisCautBdmcMod = null;

		try {
			lConn = getDBTransaction();

			lMisDao = new MisuraCautelareDAO(lConn);
			lMisCautBdmcDao = new MisuraCautelareBdmcDAO(lConn);

			for (int i = 0; i < aMisuraCautelare.size(); i++) {
				lMisMod = new MisuraCautelareModel((MisuraCautelareModel) aMisuraCautelare.get(i));

				lMisDao.setDAOFromModel(lMisMod);

				BigDecimal lKey = null;

				lKey = lMisDao.insert();

				lMisDao.stop();
				// inserisco la misura nella tabella misura_cautelare_bdmc
				lMisCautBdmcMod = (MisuraCautelareBdmcModel) aMisCautBdmc.get(i);
				lMisCautBdmcMod.setIdMisuraCautelare(lKey);

				lMisCautBdmcDao.setDAOFromModel(lMisCautBdmcMod);
				lMisCautBdmcDao.insert();
				lMisCautBdmcDao.stop();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareController.ExInserisciMisuraCautelare: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	// **********************************
	// metodo per inserire misurecautelari e posizione giuridica
	public MisuraCautelareModel ExInserisciMisuraCautelareInserisciPosizioneGiuridica(
			Vector aMisuraCautelare, PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException {

		Connection lConn = null;

		MisuraCautelareModel lMisMod = null;

		MisuraCautelareDAO lMisDao = null;
		// PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;

		try {
			lConn = getDBTransaction();

			lMisDao = new MisuraCautelareDAO(lConn);
			for (int i = 0; i < aMisuraCautelare.size(); i++) {
				lMisMod = new MisuraCautelareModel((MisuraCautelareModel) aMisuraCautelare.get(i));

				lMisDao.setDAOFromModel(lMisMod);

				lMisDao.insert();

				lMisDao.stop();
			}

			// iserimento posizione giuridica se utente ha inserito nella prima sezione
			// inserisce una misura cautelare con
			// flag computabile = 'S' and data-fine = null

			// lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			if (aMisuraCautelare != null) {
				lMisMod = (MisuraCautelareModel) aMisuraCautelare.get(0);

				BigDecimal lIdFasc = lMisMod.getFasSieIdFascicoloSiep();
				// BigDecimal lIdPosGiuCorrente = this.getIdPosizioneGiuridicaCorrente(lIdFasc);
				if ((lMisMod.getFlagComputabile().equals("S")) && (lMisMod.getDataFine() == null)) {
					// ** CERCA LA PRIMA POSIZIONE GIURIDICA **
					PosizioneGiuridicaModel lPosMod = null;

					lPosDao = new PosizioneGiuridicaDAO(lConn);

					lPosDao.setCondizioneIdFascicoloOrderDataInserimento(lIdFasc);
					lPosDao.start();
					if (lPosDao.next())
						lPosMod = (PosizioneGiuridicaModel) lPosDao.getModel();
					lPosDao.stop();

					// SE PRESENTE LA MODIFICA
					if (lPosMod != null) {
						lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

						if (aPosizioneGiuridica != null)
							lPosDao.setCodPosizioneGiuridica(aPosizioneGiuridica.getCodPosizioneGiuridica());

						lPosDao.setDataInizio(lMisMod.getDataInizio());

						lPosDao.setCodOperatoreAggiornamento(lMisMod.getCodOperatoreInserimento());
						lPosDao.setCodUfficioAggiornamento(lMisMod.getCodUfficioInserimento());
						lPosDao.setDataAggiornamento(DateUtils.getSysDate());

						lPosDao.selByKey();
						lPosDao.update();
						lPosDao.stop();

						// ** CERCA IL LUOGO DETENZIONE ASSOCIATO ALLA POSIZIONE GIURIDICA **
						lLuoDetDao = new LuogoDetenzioneDAO(lConn);
						// SE PRESENTE LO CANCELLA
						lLuoDetDao.setCondizioneIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
						lLuoDetDao.start();
						if (lLuoDetDao.next())
							lLuoDetDao.delete();
						lLuoDetDao.stop();

						// E NE INSERISCE COMUNQUE UNO
						LuogoDetenzioneModel lLuogoDetenzione = new LuogoDetenzioneModel();

						lLuogoDetenzione.setIstDetIdIstitutoDetenzione(lMisMod
								.getIstDetIdIstitutoDetenzione());
						lLuogoDetenzione.setAltroLuogo(lMisMod.getAltroLuogoDetenzione());
						lLuogoDetenzione.setDataInizioDetenzione(lMisMod.getDataInizio());
						lLuogoDetenzione.setFasSieIdFascicoloSiep(lIdFasc);

						lLuogoDetenzione.setPosGiuIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

						if (aPosizioneGiuridica != null) {
							lLuogoDetenzione.setCodOperatoreInserimento(aPosizioneGiuridica
									.getCodOperatoreInserimento());
							lLuogoDetenzione.setCodUfficioInserimento(aPosizioneGiuridica
									.getCodUfficioInserimento());
							lLuogoDetenzione.setDataInserimento(aPosizioneGiuridica.getDataInserimento());
						}

						lLuoDetDao.setDAOFromModel(lLuogoDetenzione);
						lLuoDetDao.insert();
						lLuoDetDao.stop();
					} else { // altrimenti la inserisce
						lPosDao.setDAOFromModel(aPosizioneGiuridica);
						BigDecimal lKeyPosGiu = null;
						lKeyPosGiu = lPosDao.insert();
						lPosDao.stop();

						// - Inserisce Luogo Detenzione
						lLuoDetDao = new LuogoDetenzioneDAO(lConn);

						LuogoDetenzioneModel lLuogoDetenzione = new LuogoDetenzioneModel();

						lLuogoDetenzione.setIstDetIdIstitutoDetenzione(lMisMod
								.getIstDetIdIstitutoDetenzione());
						lLuogoDetenzione.setAltroLuogo(lMisMod.getAltroLuogoDetenzione());
						lLuogoDetenzione.setDataInizioDetenzione(lMisMod.getDataInizio());
						lLuogoDetenzione.setFasSieIdFascicoloSiep(lIdFasc);

						lLuogoDetenzione.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);

						lLuogoDetenzione.setCodOperatoreInserimento(aPosizioneGiuridica
								.getCodOperatoreInserimento());
						lLuogoDetenzione.setCodUfficioInserimento(aPosizioneGiuridica
								.getCodUfficioInserimento());
						lLuogoDetenzione.setDataInserimento(aPosizioneGiuridica.getDataInserimento());

						lLuoDetDao.setDAOFromModel(lLuogoDetenzione);
						lLuoDetDao.insert();
						lLuoDetDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("MisuraCautelareController.ExInserisciMisuraCautelare: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("MisuraCautelareController.ExInserisciMisuraCautelare: " + sqe);
		} finally {
			cleanup(lMisDao);
			// cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	// **********************************
	public Vector ExRicercaMisuraCautelare(MisuraCautelareModel aMisuraCautelare) throws F3BException {

		Connection lConn = null;
		Vector lMisuraCautelari = new Vector();
		MisuraCautelareSqlDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareSqlDAO(lConn);
			lMisDao.ricercaMisuraCautelare(aMisuraCautelare);
			lMisuraCautelari = new Vector(lMisDao.getModels());
			if (lMisuraCautelari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraCautelareController.ExRicercaMisuraCautelare: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisuraCautelari;
	}

	public Vector ExRicercaMisureCautelariByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lMisureCautelari = new Vector();
		MisuraCautelareSqlDAO lMisDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDetDao = null;
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraCautelareSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDetDao = new IstitutoDetenzioneSqlDAO(lConn);

			lMisDao.ricercaMisuraCautelareByFascicolo(aKey);
			lMisureCautelari = new Vector(lMisDao.getModels());

			// modifica relativa al tipo istituto
			Iterator itx = lMisureCautelari.iterator();
			while (itx.hasNext()) {
				MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
				lIstDetDao.ricercaIstitutoDetenzioneByKey(lMis.getIstDetIdIstitutoDetenzione());
				lIstMod = (IstitutoDetenzioneModel) lIstDetDao.getModelByKey();
				lMis.setIstitutoDetenzione(lIstMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraCautelareController.ExRicercaMisureCautelariByIdFascicolo: "
					+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lIstDetDao);
			cleanup(lConn);
		}

		return lMisureCautelari;
	}

	public Vector ExRicercaMisureCautelariByIdFascicoloNoDataNull(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lMisureCautelari = new Vector();
		MisuraCautelareSqlDAO lMisDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDetDao = null;
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraCautelareSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDetDao = new IstitutoDetenzioneSqlDAO(lConn);

			lMisDao.ricercaMisuraCautelareByFascicoloNoDataNull(aKey);
			lMisureCautelari = new Vector(lMisDao.getModels());

			// modifica relativa al tipo istituto
			Iterator itx = lMisureCautelari.iterator();
			while (itx.hasNext()) {
				MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
				lIstDetDao.ricercaIstitutoDetenzioneByKey(lMis.getIstDetIdIstitutoDetenzione());
				lIstMod = (IstitutoDetenzioneModel) lIstDetDao.getModelByKey();
				lMis.setIstitutoDetenzione(lIstMod);
			}

		} catch (DAOException daoEx) {
			throw new F3BException("MisuraCautelareController.ExRicercaMisureCautelariByIdFascicolo: "
					+ daoEx);
		} finally {
			// modifica relativa al tipo istituto
			cleanup(lMisDao);
			cleanup(lIstDetDao);
			cleanup(lConn);
		}

		return lMisureCautelari;
	}

	public Vector ExRicercaMisureCautelariByIdFascicoloSiDataNull(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lMisureCautelari = new Vector();
		MisuraCautelareSqlDAO lMisDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDetDao = null;
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraCautelareSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDetDao = new IstitutoDetenzioneSqlDAO(lConn);

			lMisDao.ricercaMisuraCautelareByFascicoloSiDataNull(aKey);
			lMisureCautelari = new Vector(lMisDao.getModels());

			// modifica relativa al tipo istituto
			Iterator itx = lMisureCautelari.iterator();
			while (itx.hasNext()) {
				MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
				lIstDetDao.ricercaIstitutoDetenzioneByKey(lMis.getIstDetIdIstitutoDetenzione());
				lIstMod = (IstitutoDetenzioneModel) lIstDetDao.getModelByKey();
				lMis.setIstitutoDetenzione(lIstMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraCautelareController.ExRicercaMisureCautelariByIdFascicolo: "
					+ daoEx);
		} finally {
			// modifica relativa al tipo istituto
			cleanup(lMisDao);
			cleanup(lIstDetDao);
			cleanup(lConn);
		}

		return lMisureCautelari;
	}

	public Vector ExRicercaMisureCautelariByIdFascicoloSoloDataInizio(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lMisureCautelari = new Vector();
		MisuraCautelareSqlDAO lMisDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDetDao = null;
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraCautelareSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDetDao = new IstitutoDetenzioneSqlDAO(lConn);

			lMisDao.ricercaMisuraCautelareByFascicoloSoloDataInizio(aKey);
			lMisureCautelari = new Vector(lMisDao.getModels());

			// modifica relativa al tipo istituto
			Iterator itx = lMisureCautelari.iterator();
			while (itx.hasNext()) {
				MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
				lIstDetDao.ricercaIstitutoDetenzioneByKey(lMis.getIstDetIdIstitutoDetenzione());
				lIstMod = (IstitutoDetenzioneModel) lIstDetDao.getModelByKey();
				lMis.setIstitutoDetenzione(lIstMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraCautelareController.ExRicercaMisureCautelariByIdFascicolo: "
					+ daoEx);
		} finally {
			// modifica relativa al tipo istituto
			cleanup(lMisDao);
			cleanup(lIstDetDao);
			cleanup(lConn);
		}

		return lMisureCautelari;
	}

	// misura cautelare con data fine = null
	public MisuraCautelareModel ExRicercaMisuraCautelareSenzaDataFineByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;

		MisuraCautelareSqlDAO lMisDao = null;
		MisuraCautelareModel lMisMod;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraCautelareSqlDAO(lConn);

			lMisDao.ricercaMisuraCautelareSenzaDataFineByKey(aKey);
			lMisMod = (MisuraCautelareModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareController.ExRicercaMisuraCautelareSenzaDataFineByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	public MisuraCautelareModel ExRicercaMisuraCautelareByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraCautelareSqlDAO lMisDao = null;
		MisuraCautelareModel lMisMod;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneModel lIstMod = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareSqlDAO(lConn);
			lMisDao.ricercaMisuraCautelareByKey(aKey);
			lMisMod = (MisuraCautelareModel) lMisDao.getModelByKey();

			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			if (lMisMod != null && lMisMod.getIstDetIdIstitutoDetenzione() != null) {
				lIstDao.ricercaIstitutoDetenzioneByKey(lMisMod.getIstDetIdIstitutoDetenzione());
				lIstMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
			}
			lMisMod.setIstitutoDetenzione(lIstMod);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareController.ExRicercaMisuraCautelareByKey: Non posso leggere : " + daoEx);
		} finally {
			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	public MisuraCautelareModel ExModificaMisuraCautelare(MisuraCautelareModel aMisuraCautelare)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareDAO lMisDao = null;
		MisuraCautelareModel lMisMod = new MisuraCautelareModel(aMisuraCautelare);

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareDAO(lConn);

			String lFlagComp = aMisuraCautelare.getFlagComputabile();
			Date lDataFine = aMisuraCautelare.getDataFine();

			lMisDao.setCodOperatoreAggiornamento(lMisMod.getCodOperatoreAggiornamento());
			lMisDao.setCodUfficioAggiornamento(lMisMod.getCodUfficioAggiornamento());
			lMisDao.setDataAggiornamento(DateUtils.getSysDate());
			lMisDao.setCodTipoMisura(lMisMod.getCodTipoMisura());
			lMisDao.setIdMisuraCautelare(lMisMod.getIdMisuraCautelare());

			lMisDao.setAnnoFascBdmc(lMisMod.getAnnoFascBdmc());
			lMisDao.setNumeFascBdmc(lMisMod.getNumeFascBdmc());
			lMisDao.setAnnoRgnr(lMisMod.getAnnoRgnr());
			lMisDao.setNumeroRgnr(lMisMod.getNumeroRgnr());
			lMisDao.setAnnoRegGen(lMisMod.getAnnoRegGen());
			lMisDao.setNumeroRegGen(lMisMod.getNumeroRegGen());
			lMisDao.setTipoUfficioRegGen(lMisMod.getTipoUfficioRegGen());

			lMisDao.setAutoritaCompetente(lMisMod.getAutoritaCompetente());
			lMisDao.setAutoritaCompetenteIndirizzo(lMisMod.getAutoritaCompetenteIndirizzo());
			lMisDao.setAutoritaCompetenteSede(lMisMod.getAutoritaCompetenteSede());

			lMisDao.setDataEmissioneOrdinanza(lMisMod.getDataEmissioneOrdinanza());
			lMisDao.setCodTipoUfficioRifer(lMisMod.getCodTipoUfficioRifer());
			lMisDao.setAutoritaEmittente(lMisMod.getAutoritaEmittente());
			lMisDao.setAutoritaEmittenteLuogo(lMisMod.getAutoritaEmittenteLuogo());
			lMisDao.setNote(lMisMod.getNote());
			lMisDao.setAnnoRifer(lMisMod.getAnnoRifer());
			lMisDao.setNumRifer(lMisMod.getNumRifer());

			if (lFlagComp.equals("S") && lDataFine == null) {
				// modifica relativa al tipo istituto
				lMisDao.setIstDetIdIstitutoDetenzione(lMisMod.getIstDetIdIstitutoDetenzione());
				// lMisDao.setCodTipoIstitutoDetenzione(lMisMod.getCodTipoIstitutoDetenzione());
				lMisDao.setAltroLuogoDetenzione(lMisMod.getAltroLuogoDetenzione());
				// lMisDao.setCodLuogoDetenzione(lMisMod.getCodLuogoDetenzione());
				lMisDao.setDataInizio(lMisMod.getDataInizio());
				lMisDao.setGiorni(lMisMod.getGiorni());
				lMisDao.setFlagModificaManuale(lMisMod.getFlagModificaManuale());
				lMisDao.setAnnoRgnr(lMisMod.getAnnoRgnr());
				lMisDao.setNumeroRgnr(lMisMod.getNumeroRgnr());
				lMisDao.setAnnoFascBdmc(lMisMod.getAnnoFascBdmc());
				lMisDao.setNumeFascBdmc(lMisMod.getNumeFascBdmc());
				lMisDao.setAnnoRegGen(lMisMod.getAnnoRegGen());
				lMisDao.setNumeroRegGen(lMisMod.getNumeroRegGen());
				lMisDao.setTipoUfficioRegGen(lMisMod.getTipoUfficioRegGen());

				lMisDao.selByKey();
				lMisDao.update();
				lMisDao.stop();
			} else if (lFlagComp.equals("S") && lDataFine != null) {
				// modifica relativa al tipo istituto
				lMisDao.setIstDetIdIstitutoDetenzione(lMisMod.getIstDetIdIstitutoDetenzione());
				// lMisDao.setCodTipoIstitutoDetenzione(lMisMod.getCodTipoIstitutoDetenzione());
				lMisDao.setAltroLuogoDetenzione(lMisMod.getAltroLuogoDetenzione());
				// lMisDao.setCodLuogoDetenzione(lMisMod.getCodLuogoDetenzione());
				lMisDao.setDataInizio(lMisMod.getDataInizio());
				lMisDao.setDataFine(lMisMod.getDataFine());
				lMisDao.setNumAnni(lMisMod.getNumAnni());
				lMisDao.setNumMesi(lMisMod.getNumMesi());
				lMisDao.setNumGiorni(lMisMod.getNumGiorni());
				lMisDao.setGiorni(lMisMod.getGiorni());
				lMisDao.setFlagModificaManuale(lMisMod.getFlagModificaManuale());
				lMisDao.setNote(lMisMod.getNote());
				lMisDao.setCodMotivoNonComputabile(lMisMod.getCodMotivoNonComputabile());
				lMisDao.setAnnoRgnr(lMisMod.getAnnoRgnr());
				lMisDao.setNumeroRgnr(lMisMod.getNumeroRgnr());
				lMisDao.setAnnoFascBdmc(lMisMod.getAnnoFascBdmc());
				lMisDao.setNumeFascBdmc(lMisMod.getNumeFascBdmc());
				lMisDao.setAnnoRegGen(lMisMod.getAnnoRegGen());
				lMisDao.setNumeroRegGen(lMisMod.getNumeroRegGen());
				lMisDao.setTipoUfficioRegGen(lMisMod.getTipoUfficioRegGen());
				lMisDao.setDataFungibilita(lMisMod.getDataFungibilita());
				lMisDao.selByKey();
				lMisDao.update();
				lMisDao.stop();
			} else {
				// modifica relativa al tipo istituto
				lMisDao.setIstDetIdIstitutoDetenzione(lMisMod.getIstDetIdIstitutoDetenzione());
				// lMisDao.setCodTipoIstitutoDetenzione(lMisMod.getCodTipoIstitutoDetenzione());
				lMisDao.setAltroLuogoDetenzione(lMisMod.getAltroLuogoDetenzione());
				// lMisDao.setCodLuogoDetenzione(lMisMod.getCodLuogoDetenzione());
				lMisDao.setDataInizio(lMisMod.getDataInizio());
				lMisDao.setDataFine(lMisMod.getDataFine());
				lMisDao.setCodMotivoNonComputabile(lMisMod.getCodMotivoNonComputabile());
				lMisDao.setCodTipoUfficioRifer(lMisMod.getCodTipoUfficioRifer());
				lMisDao.setCodLuogoUfficioRifer(lMisMod.getCodLuogoUfficioRifer());
				lMisDao.setNumRifer(lMisMod.getNumRifer());
				lMisDao.setDataFungibilita(lMisMod.getDataFungibilita());
				lMisDao.setNote(lMisMod.getNote());
				lMisDao.setNumAnni(lMisMod.getNumAnni());
				lMisDao.setNumMesi(lMisMod.getNumMesi());
				lMisDao.setNumGiorni(lMisMod.getNumGiorni());
				lMisDao.setGiorni(lMisMod.getGiorni());
				lMisDao.setFlagModificaManuale(lMisMod.getFlagModificaManuale());
				lMisDao.setAnnoRgnr(lMisMod.getAnnoRgnr());
				lMisDao.setNumeroRgnr(lMisMod.getNumeroRgnr());
				lMisDao.setAnnoFascBdmc(lMisMod.getAnnoFascBdmc());
				lMisDao.setNumeFascBdmc(lMisMod.getNumeFascBdmc());
				lMisDao.setAnnoRegGen(lMisMod.getAnnoRegGen());
				lMisDao.setNumeroRegGen(lMisMod.getNumeroRegGen());
				lMisDao.setTipoUfficioRegGen(lMisMod.getTipoUfficioRegGen());
				lMisDao.selByKey();
				lMisDao.update();
				lMisDao.stop();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareController.ExModificaMisuraCautelare: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	// ****************metodo che inserisce misure e posizione giuridica****************
	public MisuraCautelareModel ExModificaMisuraCautelareModificaPosizioneGiuridica(
			MisuraCautelareModel aMisuraCautelare, PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareDAO lMisDao = null;
		MisuraCautelareModel lMisMod = new MisuraCautelareModel(aMisuraCautelare);
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;

		try {
			lConn = getDBTransaction();
			lMisDao = new MisuraCautelareDAO(lConn);

			String lFlagComp = aMisuraCautelare.getFlagComputabile();
			Date lDataFine = aMisuraCautelare.getDataFine();

			lMisDao.setCodOperatoreAggiornamento(lMisMod.getCodOperatoreAggiornamento());
			lMisDao.setCodUfficioAggiornamento(lMisMod.getCodUfficioAggiornamento());
			lMisDao.setDataAggiornamento(DateUtils.getSysDate());
			lMisDao.setCodTipoMisura(lMisMod.getCodTipoMisura());
			lMisDao.setIdMisuraCautelare(lMisMod.getIdMisuraCautelare());

			if (lFlagComp.equals("S") && lDataFine == null) {
				// modifica relativa al tipo istituto
				lMisDao.setIstDetIdIstitutoDetenzione(lMisMod.getIstDetIdIstitutoDetenzione());
				// lMisDao.setCodTipoIstitutoDetenzione(lMisMod.getCodTipoIstitutoDetenzione());
				lMisDao.setAltroLuogoDetenzione(lMisMod.getAltroLuogoDetenzione());
				// lMisDao.setCodLuogoDetenzione(lMisMod.getCodLuogoDetenzione());
				lMisDao.setDataInizio(lMisMod.getDataInizio());

				lMisDao.selByKey();
				lMisDao.update();
				lMisDao.stop();
			} else if (lFlagComp.equals("S") && lDataFine != null) {
				// modifica relativa al tipo istituto
				lMisDao.setIstDetIdIstitutoDetenzione(lMisMod.getIstDetIdIstitutoDetenzione());
				// lMisDao.setCodTipoIstitutoDetenzione(lMisMod.getCodTipoIstitutoDetenzione());
				lMisDao.setAltroLuogoDetenzione(lMisMod.getAltroLuogoDetenzione());
				// lMisDao.setCodLuogoDetenzione(lMisMod.getCodLuogoDetenzione());
				lMisDao.setDataInizio(lMisMod.getDataInizio());
				lMisDao.setDataFine(lMisMod.getDataFine());
				lMisDao.setNumAnni(lMisMod.getNumAnni());
				lMisDao.setNumMesi(lMisMod.getNumMesi());
				lMisDao.setNumGiorni(lMisMod.getNumGiorni());
				lMisDao.selByKey();
				lMisDao.update();
				lMisDao.stop();
			} else {
				// modifica relativa al tipo istituto
				lMisDao.setIstDetIdIstitutoDetenzione(lMisMod.getIstDetIdIstitutoDetenzione());
				// lMisDao.setCodTipoIstitutoDetenzione(lMisMod.getCodTipoIstitutoDetenzione());
				lMisDao.setAltroLuogoDetenzione(lMisMod.getAltroLuogoDetenzione());
				// lMisDao.setCodLuogoDetenzione(lMisMod.getCodLuogoDetenzione());
				lMisDao.setDataInizio(lMisMod.getDataInizio());
				lMisDao.setDataFine(lMisMod.getDataFine());
				lMisDao.setCodMotivoNonComputabile(lMisMod.getCodMotivoNonComputabile());
				lMisDao.setCodTipoUfficioRifer(lMisMod.getCodTipoUfficioRifer());
				lMisDao.setCodLuogoUfficioRifer(lMisMod.getCodLuogoUfficioRifer());
				lMisDao.setNumRifer(lMisMod.getNumRifer());
				lMisDao.setDataFungibilita(lMisMod.getDataFungibilita());
				lMisDao.setNote(lMisMod.getNote());
				lMisDao.setNumAnni(lMisMod.getNumAnni());
				lMisDao.setNumMesi(lMisMod.getNumMesi());
				lMisDao.setNumGiorni(lMisMod.getNumGiorni());
				lMisDao.selByKey();
				lMisDao.update();
				lMisDao.stop();
			}
			// ***************modifica Posizione giuridica****************************
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			if ((lMisMod.getFlagComputabile().equals("S")) && (lMisMod.getDataFine() == null)) {
				BigDecimal lIdFasc = lMisMod.getFasSieIdFascicoloSiep();
				lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lIdFasc);

				PosizioneGiuridicaModel lPosMod = null;
				lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

				PosizioneGiuridicaDAO lPosDao = new PosizioneGiuridicaDAO(lConn);

				if (lMisMod.getCodTipoMisura().equals("CA"))
					lPosMod.setCodPosizioneGiuridica("01");
				if (lMisMod.getCodTipoMisura().equals("AD"))
					lPosMod.setCodPosizioneGiuridica("02");
				if (lPosMod != null) {
					// update
					lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
					lPosDao.setCodOperatoreAggiornamento(lMisMod.getCodOperatoreInserimento());
					lPosDao.setCodUfficioAggiornamento(lMisMod.getCodUfficioInserimento());
					lPosDao.setDataAggiornamento(DateUtils.getSysDate());
					lPosDao.setDataFine(lMisMod.getDataInizio());
					lPosDao.selByKey();
					lPosDao.update();
					lPosDao.stop();
				}
				// insert
				lPosMod.setDataInizio(lMisMod.getDataInizio());
				lPosMod.setFasSieIdFascicoloSiep(lMisMod.getFasSieIdFascicoloSiep());
				lPosMod.setCodOperatoreInserimento(lMisMod.getCodOperatoreInserimento());
				lPosMod.setCodUfficioInserimento(lMisMod.getCodUfficioInserimento());
				lPosMod.setCodPosizioneProcessuale("-");
				lPosMod.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDAOFromModel(aPosizioneGiuridica);
				lPosDao.insert();
				lPosDao.stop();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareController.ExModificaMisuraCautelare: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lPosSqlDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	public void ExCancellaMisuraCautelare(MisuraCautelareModel aMisuraCautelare) throws F3BException {

		Connection lConn = null;
		MisuraCautelareDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareDAO(lConn);
			lMisDao.setCondizioneUpdate(aMisuraCautelare.getIdMisuraCautelare());
			lMisDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareController.ExCancellaMisuraCautelare: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	private void setDateMisura(MisuraCautelareModel lMod) {

		if (((lMod.getDataInizio()) != null) && ((lMod.getDataFine()) != null)) {
			long lDiffDate = lMod.getDataFine().getTime() - lMod.getDataInizio().getTime();
			long lOre = lDiffDate / 3600000;
			long lGiorni = lOre / 24;
			long lMesi = lGiorni / 30;
			long lAnni = lMesi / 12;
			lGiorni = lGiorni - (lMesi * 30);
			lMesi = lMesi - (lAnni * 12);
			lMod.setNumAnni(new BigDecimal(lAnni));
			lMod.setNumMesi(new BigDecimal(lMesi));
			lMod.setNumGiorni(new BigDecimal(lGiorni));
		}
	}

	public String ExInserisciMisuraCautelareWithoutSequence(ArrayList aMisuraCautelare, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		MisuraCautelareDAO lMisDao = null;
		MisuraCautelareModel lMisMod = null;

		try {
			lMisDao = new MisuraCautelareDAO(lConn);

			if (aMisuraCautelare != null && aMisuraCautelare.size() > 0) {
				for (int i = 0; i < aMisuraCautelare.size(); i++) {
					lMisMod = new MisuraCautelareModel((MisuraCautelareModel) aMisuraCautelare.get(i));

					lMisDao.setDAOFromModel(lMisMod);
					lMisDao.setWithoutSequence(true);
					lMisDao.insert();
					lMisDao.stop();
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire le Misure Cautelari! ");
			}
		} finally {
			cleanup(lMisDao);
		}

		return lCodEsito;
	}

}