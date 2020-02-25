package siap.siep.posizione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import siap.controller.SiapController;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PosizioneGiuridicaLuogoDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per PosizioneGiuridica
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
public class PosizioneGiuridicaLuogoDetenzioneController extends SiapController implements
		IPosizioneGiuridicaLuogoDetenzione {

	/**
	 * Ricerca le posizioni giuridiche
	 * 
	 * @param aPosizioneGiuridica
	 * @return Vector
	 * @throws F3BException
	 */

	public Vector ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;

		IstitutoDetenzioneModel lIstDetMod = null;
//		LuogoDetenzioneModel lLuoDet = null;
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = null;
		Vector lPosizioneLuogoAltraCausa = new Vector();
		Vector lPosizioni = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);

			// Ricerca Posizioni
			lPosDao.ricercaPosizioneGiuridicaByIdFascicolo(aIdFascicolo);
			lPosizioni = new Vector(lPosDao.getModels());

			Iterator iter = lPosizioni.iterator();
			while (iter.hasNext()) {
				PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
				;
				lPosMod = (PosizioneGiuridicaModel) iter.next();

				lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

				// setto la posizione giuridica nel in PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
				lMod.setPosizioneGiuridica(lPosMod);

				// Luogo Detenzione
				lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

				// Istituto Detenzione
				if (lLuogoDetenzione != null && lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null) {
					lIstDetMod = new IstitutoDetenzioneModel();
					lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione.getIstDetIdIstitutoDetenzione());
					lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
				}

				// setto il luogo Detenzione giuridica nel in PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
				lMod.setLuogoDetenzione(lLuogoDetenzione);

				// carico il vettore per la return
				lPosizioneLuogoAltraCausa.addElement(lMod);

			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PosizioneGiuridicaLuogoDetenzioneController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lPosizioneLuogoAltraCausa;
	}

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey(
			BigDecimal aId) throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;

//		AltraCausaDAO lAltraCausaDao = null;
		AltraCausaSqlDAO lAltraCausaSqlDao = null;

		IstitutoDetenzioneModel lIstDetMod = null;
//		LuogoDetenzioneModel lLuoDet = null;
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltraMod = null;
		PosizioneGiuridicaModel lPosMod = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);

			// Ricerca Posizione
			lPosDao.ricercaPosizioneGiuridicaByKey(aId);
			if (lPosDao != null) {
				lPosMod = new PosizioneGiuridicaModel((PosizioneGiuridicaModel) lPosDao.getModelByKey());
			}

			if (lPosMod != null) {
				lPosAltraMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

				// setto la posizione giuridica nel in PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
				lPosAltraMod.setPosizioneGiuridica(lPosMod);

				// setto AltraCausa in PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
				lAltraCausaSqlDao = new AltraCausaSqlDAO(lConn);
				lAltraCausaSqlDao.ricercaAltraCausaByKey(lPosMod.getAltCauIdAltraCausa());
				AltraCausaModel lAltraCausa = (AltraCausaModel) lAltraCausaSqlDao.getModelByKey();
				lPosAltraMod.setAltraCausa(lAltraCausa);

				// Luogo Detenzione
				lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

				// Istituto Detenzione
				if (lLuogoDetenzione != null && lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null) {
					lIstDetMod = new IstitutoDetenzioneModel();
					lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione.getIstDetIdIstitutoDetenzione());
					lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
				}

				// setto il luogo Detenzione giuridica nel in PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
				lPosAltraMod.setLuogoDetenzione(lLuogoDetenzione);

			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PosizioneGiuridicaLuogoDetenzioneController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lPosAltraMod;
	}

	public void ExCancellaPosizioneGiuridicaLuogoDetenzioneAltraCausa(
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel aPosGiu) throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneDAO lLuoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		AltraCausaDAO lAltraDao = null;
		MisuraCautelareDAO lMisCauDao = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBTransaction();
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setCondizioneUpdate(aPosGiu.getPosizioneGiuridica().getIdPosizioneGiuridica());
			lPosDao.delete();
			lPosDao.stop();
			if (aPosGiu.getAltraCausa() != null && aPosGiu.getAltraCausa().getIdAltraCausa() != null) {
				lAltraDao = new AltraCausaDAO(lConn);
				lAltraDao.setCondizioneUpdate(aPosGiu.getAltraCausa().getIdAltraCausa());
				lAltraDao.delete();
				lAltraDao.stop();
			}
			if (aPosGiu.getLuogoDetenzione() != null
					&& aPosGiu.getLuogoDetenzione().getIdLuogoDetenzione() != null) {
				lLuoDao = new LuogoDetenzioneDAO(lConn);
				lLuoDao.setCondizioneUpdate(aPosGiu.getLuogoDetenzione().getIdLuogoDetenzione());
				lLuoDao.delete();
				lLuoDao.stop();
			}
			if (aPosGiu.getPosizioneGiuridica().getIdPosizioneGiuridica() != null) {
				lMisCauDao = new MisuraCautelareDAO(lConn);
				lMisCauDao.setCondizioneDeleteByIdPosGiuridica(aPosGiu.getPosizioneGiuridica()
						.getIdPosizioneGiuridica());
				lMisCauDao.delete();
				lMisCauDao.stop();
			}
			if (aPosGiu.getPosizioneGiuridica().getFasSieIdFascicoloSiep() != null) {
				lFasDao = new FascicoloSiepDAO(lConn);
				lFasDao.selCondizioneUpdate(aPosGiu.getPosizioneGiuridica().getFasSieIdFascicoloSiep());
				lFasDao.setFlagAltraCausa(null);
				lFasDao.update();
				lFasDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LuogoDetenzioneController.ExCancellaPosizioneGiuridicaLuogoDetenzioneAltraCausa: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lAltraDao);
			cleanup(lLuoDao);
			cleanup(lConn);
		}
	}

}