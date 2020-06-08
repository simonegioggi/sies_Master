package siap.siep.beneficio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.beneficio.dao.BeneficioDAO;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.sige.beneficio.dao.BeneficioSenSigeDAO;
import siap.sige.beneficio.model.BeneficioSigeModel;

/**
 * <p>
 * Title: BeneficioController
 * </p>
 * <p>
 * Description: Classe Controller per Beneficio
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
public class BeneficioController extends SiapController implements IBeneficio {

	public void ExInserisciBeneficio(Vector aBenefici) throws F3BException {

		Connection lConn = null;
		BeneficioDAO lBenDao = null;

		try {
			lConn = getDBConnection();
			BigDecimal lKey = null;
			for (int i = 0; i < aBenefici.size(); i++) {
				BeneficioModel aBeneficio = (BeneficioModel) aBenefici.get(i);
				lBenDao = new BeneficioDAO(lConn);
				if (lKey != null)
					lBenDao.setBenIdBeneficio(lKey);
				lBenDao.setDAOFromModel(aBeneficio);
				lKey = lBenDao.insert();
			}
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("BeneficioController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaBeneficio(BeneficioModel aBeneficio) throws F3BException {

		Connection lConn = null;
		BeneficioSqlDAO lBenDao = null;

		Vector lBeneficii = new Vector();

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficio(aBeneficio);
			lBeneficii = new Vector(lBenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("BeneficioController.ExRicercaBeneficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}
		return lBeneficii;
	}

	public BeneficioModel ExRicercaBeneficioByBenIdBeneficio(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		BeneficioSqlDAO lBenDao = null;
		BeneficioModel lBenMod;

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficioByBenIdBeneficio(aKey);
			lBenMod = (BeneficioModel) lBenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioController.ExRicercaBeneficioByBenIdBeneficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}
		return lBenMod;
	}

	public BeneficioModel ExRicercaBeneficioByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		BeneficioSqlDAO lBenDao = null;
		BeneficioModel lBenMod;

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioSqlDAO(lConn);
			lBenDao.ricercaBeneficioByKey(aKey);
			lBenMod = (BeneficioModel) lBenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("BeneficioController.ExRicercaBeneficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}
		return lBenMod;
	}

	public BeneficioModel ExModificaBeneficio(BeneficioModel aBeneficio) throws F3BException {

		Connection lConn = null;
		BeneficioDAO lBenDao = null;

		BeneficioModel lBenMod = new BeneficioModel(aBeneficio);

		try {
			lConn = getDBConnection();

			lBenDao = new BeneficioDAO(lConn);
			lBenDao.setDAOFromModelForUpdate(lBenMod);
			lBenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("BeneficioController.ExModificaBeneficio: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}

		return lBenMod;
	}

	public void ExCancellaBeneficio(BeneficioModel aBeneficio) throws F3BException {

		Connection lConn = null;
		BeneficioDAO lBenDao = null;

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioDAO(lConn);
			lBenDao.setCondizioneUpdate(aBeneficio.getIdBeneficio());
			lBenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {

			throw new F3BException("BeneficioController.ExCancellaBeneficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciBeneficioWithoutSequence(ArrayList aBenefici, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		BeneficioDAO lBenDao = null;

		try {
			for (int i = 0; i < aBenefici.size(); i++) {
				BeneficioModel aBeneficio = (BeneficioModel) aBenefici.get(i);
				lBenDao = new BeneficioDAO(lConn);
				lBenDao.setDAOFromModel(aBeneficio);
				lBenDao.setWithoutSequence(true);
				lBenDao.insert();
				lBenDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserirei Benefici! ");
			}
		} finally {
			cleanup(lBenDao);

		}
		return lCodEsito;
	}

	/**
	 * ExInserisciBeneficioTipOrario
	 *
	 * @param aBenMod
	 * @param aTipologie
	 * @return aBeneficio
	 * @throws F3BException
	 */

	public BeneficioModel ExInserisciBeneficioTipOrario(BeneficioModel aBenMod, ArrayList aTipologie,
			String[] aIdPenAcc, BeneficioModel aBenNMMod) throws F3BException {

		Connection lConn = null;
		BeneficioDAO lBenDao = null;
		TipologiaOrarioDAO lTipOrDao = null;
		PenaAccessoriaDAO lPenAccDao = null;
		ScadenzarioDAO lScaDao = null;
		FascicoloSiepSqlDAO lFasSql = null;
		ScadenzarioSqlDAO lScaSqlDao = null;

		BeneficioModel aBeneficio = new BeneficioModel(aBenMod);

		try {
			lConn = getDBTransaction();

			// inserimento benefici
			lBenDao = new BeneficioDAO(lConn);
			BigDecimal lKey = null;

			// indulto o amnistia
			if ("03".equals(aBenMod.getCodTipoBeneficio()) || "04".equals(aBenMod.getCodTipoBeneficio())) {
				lBenDao.setDAOFromModel(aBenMod);
				lKey = lBenDao.insert();
				aBeneficio.setIdBeneficio(lKey);
				lBenDao.stop();
			} else { // sospensione condizionale
				if (!(aBenNMMod != null && "-".equals(aBenMod.getCodSottotipoBeneficio()))) {
					lBenDao.setDAOFromModel(aBenMod);
					lKey = lBenDao.insert();
					aBeneficio.setIdBeneficio(lKey);
					lBenDao.stop();
				}

				// inserimento beneficio non menzione
				if (aBenNMMod != null) {
					lBenDao.setDAOFromModel(aBenNMMod);
					lBenDao.setBenIdBeneficio(lKey);
					BigDecimal lKeyNM = lBenDao.insert();
					aBenNMMod.setIdBeneficio(lKeyNM);
					lBenDao.stop();

					if (lKey == null) {
						aBeneficio = aBenNMMod;
					}
				}
			}

			// Beneficio SIGE.
			if (aBenMod instanceof BeneficioSigeModel) {
				// Inserimento record di relazione in BENEFICIO_SENTENZA_SIGE
				BeneficioSenSigeDAO lBenSigeDAO = new BeneficioSenSigeDAO(lConn);
				lBenSigeDAO.setBeneficioId(aBeneficio.getIdBeneficio());
				lBenSigeDAO.setFasSigeSenId(((BeneficioSigeModel) aBenMod).getFasSigeSenId());
				lBenSigeDAO.insert();
				lBenSigeDAO.stop();
				cleanup(lBenSigeDAO);
			}

			lPenAccDao = new PenaAccessoriaDAO(lConn);
			if (aIdPenAcc != null && aIdPenAcc.length > 0) {
				for (int i = 0; i < aIdPenAcc.length; i++) {
					lPenAccDao.setBenIdBeneficio(lKey);
					lPenAccDao.selCondizioneUpdate(new BigDecimal(aIdPenAcc[i]));
					lPenAccDao.update();
				}
			}
			lPenAccDao.stop();

			// inserimento tipologia Orario
			lTipOrDao = new TipologiaOrarioDAO(lConn);

			if (aTipologie != null) {
				Iterator iter = aTipologie.iterator();
				while (iter.hasNext()) {
					TipologiaOrarioModel TipoOrMod = (TipologiaOrarioModel) iter.next();
					lTipOrDao.setDAOFromModel(TipoOrMod);
					lTipOrDao.setBenIdBeneficio(lKey);
					lTipOrDao.insert();
				}
			}

			lTipOrDao.stop();

			// ricerca fascicolo siep
			if (aBeneficio.getFasSieIdFascicoloSiep() != null) {
				lFasSql = new FascicoloSiepSqlDAO(lConn);
				lFasSql.ricercaFascicoloByKey(aBeneficio.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFascMod = (FascicoloSiepModel) lFasSql.getModelByKey();

				// inserita correzione che non fa inserire gli scadenzari per classi diverse dalla III
				// come da richiesta di Michele Testa a9-rr-074. aagiungo anche che se esiste lo scadenzario
				// non lo trovo
				// scadenzario sospensione condizionale -- va inserito sempre Michele Testa per classi III

				if (lFascMod.getChiaveProgr().intValue() > 30000
						&& lFascMod.getChiaveProgr().intValue() < 40000) {
					// scadenzario termini ottemperanza obblighi tipo 16
					if ("01".equals(aBenMod.getCodTipoBeneficio())
							&& "03".equals(aBenMod.getCodSottotipoBeneficio())) {
						if (aBenMod.getNumAnniAdempimento().intValue() == 0
								&& aBenMod.getNumMesiAdempimento().intValue() == 0
								&& aBenMod.getNumGiorniAdempimento().intValue() == 0) {
							// non viene inserito lo scadenzario
						} else {
							lScaSqlDao = new ScadenzarioSqlDAO(lConn);
							lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("16",
									lFascMod.getIdFascicoloSiep());
							ScadenzarioModel lScaMod = new ScadenzarioModel();
							lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
							if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
								// se esiste non faccio nulla
							} else {
								Date lSommaAnni = null;
								Date lSommaMesi = null;
								Date lFineScadenza = null;
								String lDataInizio = DateUtils
										.getDateToString(lFascMod.getDataIrrevocabilita(), "dd/MM/yyyy");
								lSommaAnni = DateUtils.moveDateTo(
										DateUtils.getDate(lDataInizio, "dd/MM/yyyy"), java.util.Calendar.YEAR,
										aBenMod.getNumAnniAdempimento().intValue());
								lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
										aBenMod.getNumMesiAdempimento().intValue());
								lFineScadenza = DateUtils.moveDateTo(lSommaMesi,
										java.util.Calendar.DAY_OF_MONTH,
										aBenMod.getNumGiorniAdempimento().intValue());

								lScaDao = new ScadenzarioDAO(lConn);
								lScaDao.setCodTipoScadenzario("16");
								lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
								lScaDao.setDataFineScadenza(lFineScadenza);
								lScaDao.setFasSieIdFascicoloSiep(aBeneficio.getFasSieIdFascicoloSiep());
								lScaDao.setEveIdEvento(aBeneficio.getEveIdEvento());
								lScaDao.setCodOperatoreInserimento(aBeneficio.getCodOperatoreInserimento());
								lScaDao.setCodUfficioInserimento(aBeneficio.getCodUfficioInserimento());
								lScaDao.setDataInserimento(DateUtils.getSysDate());
								lScaDao.setFlagVisto("N");
								lScaDao.setCodStatoNotifica("N");

								lScaDao.insert();
							}
						}
					}

					// scadenzario sospensione condizionale -- va inserito sempre Michele Testa tipo 17
					if ("01".equals(aBenMod.getCodTipoBeneficio())) {
						lScaSqlDao = new ScadenzarioSqlDAO(lConn);
						lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("17",
								lFascMod.getIdFascicoloSiep());
						ScadenzarioModel lScaMod = new ScadenzarioModel();
						lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
						if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
							// se esiste non faccio nulla
						} else {
							Date lFineScadenza = null;
							String lDataInizio = DateUtils.getDateToString(lFascMod.getDataIrrevocabilita(),
									"dd/MM/yyyy");

							lFineScadenza = DateUtils.moveDateTo(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"),
									java.util.Calendar.YEAR, aBenMod.getNumAnniSospensione().intValue());

							lScaDao = new ScadenzarioDAO(lConn);
							lScaDao.setCodTipoScadenzario("17");
							lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
							lScaDao.setDataFineScadenza(lFineScadenza);
							lScaDao.setFasSieIdFascicoloSiep(aBeneficio.getFasSieIdFascicoloSiep());
							lScaDao.setEveIdEvento(aBeneficio.getEveIdEvento());
							lScaDao.setCodOperatoreInserimento(aBeneficio.getCodOperatoreInserimento());
							lScaDao.setCodUfficioInserimento(aBeneficio.getCodUfficioInserimento());
							lScaDao.setDataInserimento(DateUtils.getSysDate());
							lScaDao.setFlagVisto("N");
							lScaDao.setCodStatoNotifica("N");

							lScaDao.insert();
						}
					}
				}
			} // endif idFascicoloSiep
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"BeneficioController.ExInserisciBeneficioTipOrario: Non posso inserire: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lTipOrDao);
			cleanup(lPenAccDao);
			cleanup(lScaDao);
			cleanup(lFasSql);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lScaSqlDao);
			cleanup(lConn);
		}

		return aBeneficio;
	}

	/**
	 * ExModificaBeneficioTipologiaOrario
	 *
	 * @param aBeneficio
	 * @param aTipologie
	 * @return
	 * @throws F3BException
	 */
	public BeneficioModel ExModificaBeneficioTipologiaOrario(BeneficioModel aBeneficio, ArrayList aTipologie,
			String[] aIdPenAcc, BeneficioModel aBenNMMod) throws F3BException {

		Connection lConn = null;
		TipologiaOrarioDAO lTipOrDao = null;
		BeneficioDAO lBenDao = null;
		BeneficioSqlDAO lBenSqlDao = null;
		PenaAccessoriaDAO lPenAccDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		FascicoloSiepSqlDAO lFasSql = null;

		BeneficioModel lBenMod = new BeneficioModel(aBeneficio);

		try {
			lConn = getDBTransaction();
			lBenDao = new BeneficioDAO(lConn);
			lBenSqlDao = new BeneficioSqlDAO(lConn);

			// solo per non menzione
			if (lBenMod != null && "02".equals(lBenMod.getCodTipoBeneficio())) {
				// cancello
				lBenDao.setCondizioneUpdate(lBenMod.getIdBeneficio());
				lBenDao.delete();
				lBenDao.stop();

				lBenSqlDao.ricercaBeneficioByBenIdBeneficio(lBenMod.getIdBeneficio());
				BeneficioModel lBenNNMMod = (BeneficioModel) lBenSqlDao.getModelByKey();
				lBenSqlDao.stop();

				// cancello
				if (lBenNNMMod != null && lBenNNMMod.getIdBeneficio() != null) {
					lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficio());
					lBenDao.delete();
					lBenDao.stop();
				}

				// inserisco
				lBenDao.setDAOFromModel(aBenNMMod);
				BigDecimal lKey = lBenDao.insert();
				lBenDao.stop();
				aBenNMMod.setIdBeneficio(lKey);

				// lBenMod = new BeneficioModel(aBenNMMod);
			} else {
				// per tutti gli altri casi
				// inserisce il beneficio
				lBenDao.setDAOFromModelForUpdate(lBenMod);
				lBenDao.update();
				lBenDao.stop();

				// gestione non menzione
				lBenSqlDao.ricercaBeneficioByBenIdBeneficio(lBenMod.getIdBeneficio());
				BeneficioModel lBenNNMMod = (BeneficioModel) lBenSqlDao.getModelByKey();

				if (lBenNNMMod != null && lBenNNMMod.getIdBeneficio() != null) {
					if (aBenNMMod == null) {
						// cancello
						lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficio());
						lBenDao.delete();
						lBenDao.stop();
					}
				} else if (aBenNMMod != null) {
					// inserisco
					lBenDao.setDAOFromModel(aBenNMMod);
					lBenDao.setBenIdBeneficio(lBenMod.getIdBeneficio());
					lBenDao.insert();
					lBenDao.stop();
				}

				// gestione pena accessoria
				// prima di aggiornare ripulisco il ben_id_benefeciodelle pene accessorie
				// in questione
				lPenAccDao = new PenaAccessoriaDAO(lConn);
				lPenAccDao.setBenIdBeneficio(null);
				lPenAccDao.selCondizioneUpdateBenIdBeneficioFascSiep(lBenMod.getIdBeneficio(),
						lBenMod.getFasSieIdFascicoloSiep());
				lPenAccDao.update();
				lPenAccDao.stop();

				if (aIdPenAcc != null && aIdPenAcc.length > 0) {
					for (int i = 0; i < aIdPenAcc.length; i++) {
						lPenAccDao.setBenIdBeneficio(lBenMod.getIdBeneficio());
						lPenAccDao.selCondizioneUpdate(new BigDecimal(aIdPenAcc[i]));
						lPenAccDao.update();
					}
				}

				lPenAccDao.stop();

				// prima d'inserire la tipologia orario cancello i recor presenti
				lTipOrDao = new TipologiaOrarioDAO(lConn);
				lTipOrDao.selCondizioneDeleteByIdBeneficio(lBenMod.getIdBeneficio());
				lTipOrDao.delete();
				lTipOrDao.stop();

				// inserimento tipologia Orario
				if (aTipologie != null) {
					Iterator iter = aTipologie.iterator();
					while (iter.hasNext()) {
						TipologiaOrarioModel TipoOrMod = (TipologiaOrarioModel) iter.next();
						lTipOrDao.setDAOFromModel(TipoOrMod);
						lTipOrDao.setBenIdBeneficio(lBenMod.getIdBeneficio());
						lTipOrDao.insert();
					}
				}

				lTipOrDao.stop();
			}

			// ricerca fascicolo siep
			if (aBeneficio.getFasSieIdFascicoloSiep() != null) {
				lFasSql = new FascicoloSiepSqlDAO(lConn);
				lFasSql.ricercaFascicoloByKey(aBeneficio.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFascMod = (FascicoloSiepModel) lFasSql.getModelByKey();

				// inserita correzione che non fa inserire gli scadenzari per classi diverse dalla III
				// come da richiesta di Michele Testa a9-rr-074. aagiungo anche che se esiste lo scadenzario
				// non lo trovo
				// scadenzario sospensione condizionale -- va inserito sempre Michele Testa per classi III
				if (lFascMod.getChiaveProgr().intValue() > 30000
						&& lFascMod.getChiaveProgr().intValue() < 40000) {
					// scadenzario termini ottemperanza obblighi tipo 16
					if ("01".equals(lBenMod.getCodTipoBeneficio())
							&& "03".equals(lBenMod.getCodSottotipoBeneficio())) {
						lScaDao = new ScadenzarioDAO(lConn);
						lScaSqlDao = new ScadenzarioSqlDAO(lConn);
						lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("16",
								lFascMod.getIdFascicoloSiep());
						ScadenzarioModel lScaMod = new ScadenzarioModel();
						lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
						if (lBenMod.getNumAnniAdempimento().intValue() == 0
								&& lBenMod.getNumMesiAdempimento().intValue() == 0
								&& lBenMod.getNumGiorniAdempimento().intValue() == 0) {
							// se in modifica azzero i termini ottemperanza obblighi cancello lo scadenzario
							lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(
									lBenMod.getFasSieIdFascicoloSiep(), "16");
							lScaDao.delete();
							lScaDao.stop();
						} else {
							if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
								// se esiste faccio la modifica
								Date lSommaAnni = null;
								Date lSommaMesi = null;
								Date lFineScadenza = null;

								String lDataInizio = DateUtils
										.getDateToString(lFascMod.getDataIrrevocabilita(), "dd/MM/yyyy");

								lSommaAnni = DateUtils.moveDateTo(
										DateUtils.getDate(lDataInizio, "dd/MM/yyyy"), java.util.Calendar.YEAR,
										lBenMod.getNumAnniAdempimento().intValue());
								lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
										lBenMod.getNumMesiAdempimento().intValue());
								lFineScadenza = DateUtils.moveDateTo(lSommaMesi,
										java.util.Calendar.DAY_OF_MONTH,
										lBenMod.getNumGiorniAdempimento().intValue());

								lScaDao.setCodTipoScadenzario("16");
								lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
								lScaDao.setDataFineScadenza(lFineScadenza);
								lScaDao.setFasSieIdFascicoloSiep(aBeneficio.getFasSieIdFascicoloSiep());
								lScaDao.setEveIdEvento(aBeneficio.getEveIdEvento());
								lScaDao.setCodOperatoreInserimento(aBeneficio.getCodOperatoreInserimento());
								lScaDao.setCodUfficioInserimento(aBeneficio.getCodUfficioInserimento());
								lScaDao.setDataInserimento(DateUtils.getSysDate());
								lScaDao.setFlagVisto("N");
								lScaDao.setCodStatoNotifica("N");
								lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
								lScaDao.update();
							} else {
								Date lSommaAnni = null;
								Date lSommaMesi = null;
								Date lFineScadenza = null;

								String lDataInizio = DateUtils
										.getDateToString(lFascMod.getDataIrrevocabilita(), "dd/MM/yyyy");

								lSommaAnni = DateUtils.moveDateTo(
										DateUtils.getDate(lDataInizio, "dd/MM/yyyy"), java.util.Calendar.YEAR,
										lBenMod.getNumAnniAdempimento().intValue());
								lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
										lBenMod.getNumMesiAdempimento().intValue());
								lFineScadenza = DateUtils.moveDateTo(lSommaMesi,
										java.util.Calendar.DAY_OF_MONTH,
										lBenMod.getNumGiorniAdempimento().intValue());

								lScaDao = new ScadenzarioDAO(lConn);
								lScaDao.setCodTipoScadenzario("16");
								lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
								lScaDao.setDataFineScadenza(lFineScadenza);
								lScaDao.setFasSieIdFascicoloSiep(aBeneficio.getFasSieIdFascicoloSiep());
								lScaDao.setEveIdEvento(aBeneficio.getEveIdEvento());
								lScaDao.setCodOperatoreInserimento(aBeneficio.getCodOperatoreInserimento());
								lScaDao.setCodUfficioInserimento(aBeneficio.getCodUfficioInserimento());
								lScaDao.setDataInserimento(DateUtils.getSysDate());
								lScaDao.setFlagVisto("N");
								lScaDao.setCodStatoNotifica("N");

								lScaDao.insert();
							}
						}
					}

					// scadenzario sospensione condizionale -- va inserito sempre Michele Testa tipo 17
					if ("01".equals(lBenMod.getCodTipoBeneficio())) {
						lScaSqlDao = new ScadenzarioSqlDAO(lConn);
						lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("17",
								lFascMod.getIdFascicoloSiep());
						ScadenzarioModel lScaMod = new ScadenzarioModel();
						lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
						if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
							// se esiste faccio la modifica
							Date lFineScadenza = null;
							String lDataInizio = DateUtils.getDateToString(lFascMod.getDataIrrevocabilita(),
									"dd/MM/yyyy");

							lFineScadenza = DateUtils.moveDateTo(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"),
									java.util.Calendar.YEAR, lBenMod.getNumAnniSospensione().intValue());

							lScaDao = new ScadenzarioDAO(lConn);
							lScaDao.setCodTipoScadenzario("17");
							lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
							lScaDao.setDataFineScadenza(lFineScadenza);
							lScaDao.setFasSieIdFascicoloSiep(aBeneficio.getFasSieIdFascicoloSiep());
							lScaDao.setEveIdEvento(aBeneficio.getEveIdEvento());
							lScaDao.setCodOperatoreInserimento(aBeneficio.getCodOperatoreInserimento());
							lScaDao.setCodUfficioInserimento(aBeneficio.getCodUfficioInserimento());
							lScaDao.setDataInserimento(DateUtils.getSysDate());
							lScaDao.setFlagVisto("N");
							lScaDao.setCodStatoNotifica("N");
							lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
							lScaDao.update();
						} else {
							Date lFineScadenza = null;
							String lDataInizio = DateUtils.getDateToString(lFascMod.getDataIrrevocabilita(),
									"dd/MM/yyyy");

							lFineScadenza = DateUtils.moveDateTo(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"),
									java.util.Calendar.YEAR, lBenMod.getNumAnniSospensione().intValue());

							lScaDao = new ScadenzarioDAO(lConn);
							lScaDao.setCodTipoScadenzario("17");
							lScaDao.setDataInizioScadenza(DateUtils.getDate(lDataInizio, "dd/MM/yyyy"));
							lScaDao.setDataFineScadenza(lFineScadenza);
							lScaDao.setFasSieIdFascicoloSiep(aBeneficio.getFasSieIdFascicoloSiep());
							lScaDao.setEveIdEvento(aBeneficio.getEveIdEvento());
							lScaDao.setCodOperatoreInserimento(aBeneficio.getCodOperatoreInserimento());
							lScaDao.setCodUfficioInserimento(aBeneficio.getCodUfficioInserimento());
							lScaDao.setDataInserimento(DateUtils.getSysDate());
							lScaDao.setFlagVisto("N");
							lScaDao.setCodStatoNotifica("N");

							lScaDao.insert();
						}
					}
				}
			} // endif idFascicoloSiep
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("BeneficioController.ExModificaBeneficioTipologiaOrario: " + ex);
		} finally {
			cleanup(lTipOrDao);
			cleanup(lBenDao);
			cleanup(lPenAccDao);
			cleanup(lBenSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lFasSql);
			cleanup(lConn);
		}

		return lBenMod;
	}

	/**
	 * ExCancellaBeneficioTipologiaOrario
	 *
	 * @param aBeneficio
	 * @throws F3BException
	 */
	public void ExCancellaBeneficioTipologiaOrario(BeneficioModel aBeneficio) throws F3BException {

		Connection lConn = null;
		BeneficioDAO lBenDao = null;
		BeneficioSqlDAO lBenSqlDao = null;
		TipologiaOrarioDAO lTipOrDao = null;
		PenaAccessoriaDAO lPenAccDao = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBTransaction();
			// Cancellazione record nella tabella di relazione per Beneficio SIGE
			if (aBeneficio instanceof BeneficioSigeModel) {
				// Cancellazione record di relazione in BENEFICIO_SENTENZA_SIGE
				BeneficioSenSigeDAO lBenSigeDAO = new BeneficioSenSigeDAO(lConn);
				lBenSigeDAO.setCondizioneDeleteBeneficio(aBeneficio.getIdBeneficio());
				lBenSigeDAO.delete();
				lBenSigeDAO.stop();
				cleanup(lBenSigeDAO);
			}

			lBenSqlDao = new BeneficioSqlDAO(lConn);
			lBenSqlDao.ricercaBeneficioByKey(aBeneficio.getIdBeneficio());
			BeneficioModel lBeneficio = (BeneficioModel) lBenSqlDao.getModelByKey();
			lBenSqlDao.stop();

			// se esiste cancello la tipologia orario
			lTipOrDao = new TipologiaOrarioDAO(lConn);
			lTipOrDao.selCondizioneDeleteByIdBeneficio(lBeneficio.getIdBeneficio());
			lTipOrDao.delete();
			lTipOrDao.stop();

			// se esistono pene accessorie legate al beneficio, viene messo a null
			// BEN_ID_BENEFICIO
			lPenAccDao = new PenaAccessoriaDAO(lConn);
			lPenAccDao.setBenIdBeneficio(null);
			lPenAccDao.selCondizioneUpdateBenIdBeneficioFascSiep(lBeneficio.getIdBeneficio(),
					lBeneficio.getFasSieIdFascicoloSiep());
			lPenAccDao.update();
			lPenAccDao.stop();

			// gestione non menzione
			lBenSqlDao.ricercaBeneficioByBenIdBeneficio(lBeneficio.getIdBeneficio());
			BeneficioModel lBenNNMMod = (BeneficioModel) lBenSqlDao.getModelByKey();

			if (lBenNNMMod != null && lBenNNMMod.getIdBeneficio() != null) {
				lBenDao = new BeneficioDAO(lConn);
				lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficio());
				lBenDao.delete();
				lBenDao.stop();
			}

			// cancellazione beneficio
			lBenDao = new BeneficioDAO(lConn);
			lBenDao.setCondizioneUpdate(lBeneficio.getIdBeneficio());
			lBenDao.delete();
			lBenDao.stop();

			// Associazione scadenzari paolo 13/01/2011 aggiungo cancellazione scadenzari
			// tipo beneficio 01 sosttotipo beneficio 01 e 02 ----> 17 Termini Sospensione Condizionale
			// tipo beneficio 01 sosttotipo beneficio 03 ---------> 16 Termini Ottemperanza Obblighi
			// (ma viene cmq creato sempre anche 17 Termini Sospensione Condizionale come chiesto da testa
			// descrizione sottobenefici
			// "03" />il giudice dispone che la pena rimanga sospesa subordinatamente all'adempimento
			// dell'obbligo
			// "02" />il giudice dispone che l'esecuzione della pena detentiva rimanga sospesa
			// "01" />il giudice dispone che l'esecuzione dell'intera pena rimanga sospesa
			if ("01".equals(lBeneficio.getCodTipoBeneficio())) {
				BeneficioModel lBenModRet = new BeneficioModel();
				lBenModRet.setCodTipoBeneficio(lBeneficio.getCodTipoBeneficio());
				lBenModRet.setFasSieIdFascicoloSiep(lBeneficio.getFasSieIdFascicoloSiep());
				lBenSqlDao.ricercaBeneficio(lBenModRet);
				lBenModRet = (BeneficioModel) lBenSqlDao.getModelByKey();
				// se non esiste nessun beneficio cancello gli eventuali scadenzari 16 e 17
				if (lBenModRet == null || lBenModRet.getIdBeneficio() == null) {
					lScaDao = new ScadenzarioDAO(lConn);
					lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(
							lBeneficio.getFasSieIdFascicoloSiep(), "16");
					lScaDao.delete();
					lScaDao.stop();

					lScaDao = new ScadenzarioDAO(lConn);
					lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(
							lBeneficio.getFasSieIdFascicoloSiep(), "17");
					lScaDao.delete();
					lScaDao.stop();
				} else {
					lBenModRet.setCodSottotipoBeneficio("03");
					lBenSqlDao.ricercaBeneficio(lBenModRet);
					lBenModRet = (BeneficioModel) lBenSqlDao.getModelByKey();
					// se non esiste sottobeneficio 03 e quindi esiste sottobeneficio 01 o 02
					// cancello solo scadenzario 16 Termini Ottemperanza Obblighi
					if (lBenModRet == null || lBenModRet.getIdBeneficio() == null) {
						lScaDao = new ScadenzarioDAO(lConn);
						lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(
								lBeneficio.getFasSieIdFascicoloSiep(), "16");
						lScaDao.delete();
						lScaDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioController.ExCancellaBeneficioTipologiaOrario: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lTipOrDao);
			cleanup(lPenAccDao);
			cleanup(lBenSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lScaDao);
			cleanup(lConn);
		}
	}

}