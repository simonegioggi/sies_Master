package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.BeneficioCumuloDAO;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloDAO;
import siap.siep.modulocumulo.dao.RichPMBeneficioCumSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.RichPMBeneficioCumModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;

/**
 * BeneficioCumuloController - Classe Controller per Beneficio_Cumulato
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BeneficioCumuloController extends SiapController implements IBeneficioCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public void ExInserisciRevocaBeneficioCumulo(BeneficioCumuloModel aBeneficioRevoca,
			BigDecimal aKeyBeneficio) throws F3BException {

		Connection lConn = null;
		BeneficioCumuloDAO lBenDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		BeneficioCumuloDAO lBenConcDao = null;

		BeneficioCumuloModel lBeneficioConcessoModel = null;

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioCumuloDAO(lConn);
			lBenDao.setDAOFromModel(aBeneficioRevoca);

			lBenDao.insert();
			lBenDao.stop();

			// Ricerca e Successiva Update sul Beneficio Concesso legato alla REVOCA appena inserita
			if (aKeyBeneficio != null && aKeyBeneficio.intValue() != 0) {
				// LogF3B.getLogger().debug(" BeneficioCumuloController - ExInserisciRevocaBeneficioCumulo -
				// aKeyBeneficioConcesso = "+aKeyBeneficio );
				lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
				lBenSqlDao.ricercaBeneficioCumuloByKey(aKeyBeneficio);
				lBeneficioConcessoModel = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();

				// Riferimento nel Beneficio_Concesso al TITOLO di Revoca_Beneficio
				lBeneficioConcessoModel
						.setTitIdTitoloCumulatoCollegato(aBeneficioRevoca.getTitIdTitoloCumulato());

				lBenConcDao = new BeneficioCumuloDAO(lConn);
				lBenConcDao.setDAOFromModelForUpdate(lBeneficioConcessoModel);
				lBenConcDao.update();
				lBenConcDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"BeneficioCumuloController.ExInserisciRevocaBeneficioCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lBenConcDao);
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}
	}

	// PARAMETRI: aKeyBeneficio = Key del BENEFICIO CONCESSO
	// aBeneficioRevoca = Model relativo al BENEFICIO REVOCA; già riempito
	// aDeassocia = SI/NO ; se SI, bisogna Eliminare il legame tra BENEFICIO REVOCA e relativo BENEFICIO
	// CONCESSO.
	public BeneficioCumuloModel ExModificaRevocaBeneficioCumulo(BeneficioCumuloModel aBeneficioRevoca,
			BigDecimal aKeyBeneficio, String aDeassocia, BigDecimal aIdBeneficioConcesso)
			throws F3BException {

		Connection lConn = null;

		BeneficioCumuloDAO lBenDao = null;
		BeneficioCumuloDAO lConcessoDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;

		BeneficioCumuloModel lBenMod = new BeneficioCumuloModel(aBeneficioRevoca);

		try {
			lConn = getDBTransaction();
			lBenDao = new BeneficioCumuloDAO(lConn);

			// Modifica la Revoca
			lBenDao.setDAOFromModelForUpdate(lBenMod);
			lBenDao.update();
			lBenDao.stop();

			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);

			// Deassocia La Revoca dal Corrispondente Beneficio Concesso Inizialmente
			if (aDeassocia.compareTo("SI") == 0 && aKeyBeneficio != null) {
				lBenSqlDao.ricercaBeneficioCumuloByKey(aKeyBeneficio);
				BeneficioCumuloModel lConcesso = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();
				lBenSqlDao.stop();

				if (lConcesso != null && lConcesso.getIdBeneficioCumulo() != null
						&& lConcesso.getTitIdTitoloCumulatoCollegato() != null) {
					lConcessoDao = new BeneficioCumuloDAO(lConn);
					lConcesso.setTitIdTitoloCumulatoCollegato(null); // Deassocia
					lConcessoDao.setDAOFromModelForUpdate(lConcesso);
					lConcessoDao.update();
					lConcessoDao.stop();
				}
			}

			if (aIdBeneficioConcesso != null && aIdBeneficioConcesso.intValue() != 0) {
				// LogF3B.getLogger().debug(" BeneficioCumuloController - ExInserisciRevocaBeneficioCumulo -
				// aKeyBeneficioConcesso = "+aKeyBeneficio );
				lBenSqlDao.ricercaBeneficioCumuloByKey(aIdBeneficioConcesso);
				BeneficioCumuloModel lBeneficioConcessoModel = (BeneficioCumuloModel) lBenSqlDao
						.getModelByKey();

				// Riferimento nel Beneficio_Concesso al TITOLO di Revoca_Beneficio
				lBeneficioConcessoModel
						.setTitIdTitoloCumulatoCollegato(aBeneficioRevoca.getTitIdTitoloCumulato());

				lBenDao.setDAOFromModelForUpdate(lBeneficioConcessoModel);
				lBenDao.update();
				lBenDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("BeneficioCumuloController.ExModificaRevocaBeneficioCumulo: " + ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lBenSqlDao);
			cleanup(lConcessoDao);
			cleanup(lConn);
		}

		return lBenMod;
	}

	public Vector ExRicercaBeneficioCumulo(BeneficioCumuloModel aBeneficio) throws F3BException {

		Connection lConn = null;
		Vector lBeneficii = new Vector();
		BeneficioCumuloSqlDAO lBenSqlDao = null;

		try {
			lConn = getDBConnection();
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lBenSqlDao.ricercaBeneficioCumulo(aBeneficio);
			lBeneficii = new Vector(lBenSqlDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioCumuloController.ExRicercaBeneficioCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}
		return lBeneficii;
	}

	public BeneficioCumuloModel ExRicercaBeneficioCumuloByBenIdBeneficioCum(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		BeneficioCumuloSqlDAO lBenDao = null;
		BeneficioCumuloModel lBenMod;

		try {
			lConn = getDBConnection();
			lBenDao = new BeneficioCumuloSqlDAO(lConn);
			lBenDao.ricercaBeneficioCumuloByBenIdBeneficioCum(aKey);
			lBenMod = (BeneficioCumuloModel) lBenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioCumuloController.ExRicercaBeneficioCumuloByBenIdBeneficioCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lConn);
		}
		return lBenMod;
	}

	public BeneficioCumuloModel ExRicercaBeneficioCumuloByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		BeneficioCumuloModel lBenMod;

		try {
			lConn = getDBConnection();
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lBenSqlDao.ricercaBeneficioCumuloByKey(aKey);
			lBenMod = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioCumuloController.ExRicercaBeneficioCumuloByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}
		return lBenMod;
	}

	public BeneficioCumuloModel ExRicercaBeneficioCumuloByKeyBeneficioOrig(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		BeneficioCumuloModel lBenMod;

		try {
			lConn = getDBConnection();
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lBenSqlDao.ricercaBeneficioCumuloByKeyBeneficioOrig(aKey);
			lBenMod = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioCumuloController.ExRicercaBeneficioCumuloByKeyBeneficioOrig: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}
		return lBenMod;
	}

	public void ExCancellaBeneficioCumuloRevoca(BeneficioCumuloModel aBeneficioRevoca) throws F3BException {

		Connection lConn = null;
		BeneficioCumuloDAO lBenDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		BeneficioCumuloDAO lConcBenDao = null;

		Vector lConcessi = new Vector();
		BeneficioCumuloModel lModel = new BeneficioCumuloModel();
		BeneficioCumuloModel ConcessoMod = null;
		try {
			// LogF3B.getLogger().debug("--XX-- ExCancellaBeneficioCumuloRevoca -Inizio" );
			lConn = getDBConnection();
			lBenDao = new BeneficioCumuloDAO(lConn);
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lConcBenDao = new BeneficioCumuloDAO(lConn);

			// RICERCA E MODIFICA BENEFICIO CONCESSO collegato ALLA REVOCA
			if (aBeneficioRevoca.getTitIdTitoloCumulatoCollegato() != null) {
				// La Revoca è collegata ad un Bemeficio Concesso;
				// Prima di Cancellare la REVOCA, Cerco e Modifico il Beneficio Concesso (che ora NON è più
				// Revocato)
				lModel.setTitIdTitoloCumulato(aBeneficioRevoca.getTitIdTitoloCumulatoCollegato());
				lModel.setCodTipoBeneficio(aBeneficioRevoca.getCodTipoBeneficio());
				lModel.setCodNaturaBeneficio("C");

				lBenSqlDao.ricercaBeneficioCumulo(lModel);
				lConcessi = new Vector(lBenSqlDao.getModels());

				if (lConcessi != null && lConcessi.size() > 0) {
					ConcessoMod = (BeneficioCumuloModel) lConcessi.get(0);
					if (ConcessoMod != null && ConcessoMod.getIdBeneficioCumulo() != null
							&& ConcessoMod.getTitIdTitoloCumulatoCollegato() != null) {
						// Modifico il Beneficio Concesso 'slegandolo' dalla REVOCA
						ConcessoMod.setTitIdTitoloCumulatoCollegato(null);

						lConcBenDao.setDAOFromModelForUpdate(ConcessoMod);
						lConcBenDao.setCondizioneUpdate(ConcessoMod.getIdBeneficioCumulo());
						lConcBenDao.update();
						lConcBenDao.stop();
					}
				}
			}

			// CANCELLAZIONE REVOCA
			// cancellazione Revoca: Fisica
			if (aBeneficioRevoca.getFlagStato().compareTo("I") == 0) {
				lBenDao.setCondizioneUpdate(aBeneficioRevoca.getIdBeneficioCumulo());
				lBenDao.delete();
				lBenDao.stop();
			} else {
				// Cancellazione logica
				aBeneficioRevoca.setFlagStato("C");
				aBeneficioRevoca.setTitIdTitoloCumulatoCollegato(null);

				lBenDao.setDAOFromModelForUpdate(aBeneficioRevoca);
				lBenDao.setCondizioneUpdate(aBeneficioRevoca.getIdBeneficioCumulo());
				lBenDao.update();
				lBenDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioCumuloController.ExCancellaBeneficioCumuloRevoca: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lBenSqlDao);
			cleanup(lConcBenDao);
			cleanup(lConn);
		}
	}

	/**
	 *
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 *
	 * @param aBenefici
	 * @param lConn
	 * @since MEV 42 Cumulo Step2
	 */
	public String ExInserisciBeneficiCumuloWithoutSequence(Vector<BeneficioCumuloModel> aBenefici,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		BeneficioCumuloDAO lBenDao = null;
		BeneficioCumuloModel aBeneficio = null;

		try {
			lBenDao = new BeneficioCumuloDAO(lConn);
			if (aBenefici != null && aBenefici.size() > 0) {
				for (int i = 0; i < aBenefici.size(); i++) {
					try {
						aBeneficio = aBenefici.get(i);
						lBenDao.setDAOFromModel(aBeneficio);
						lBenDao.setWithoutSequence(true);
						lBenDao.insert();
						lBenDao.stop();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.error("Beneficio Cumulo gia' presente..."
									+ aBeneficio.getIdBeneficioCumulo() + "<");
							lCodEsito = "00001";
						} else {
							throw ex;
						}
					}
				}
			}
		} catch (DAOException ex) {
			lCodEsito = "01400";
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserirei Benefici Cumulo! ");
		} finally {
			cleanup(lBenDao);
		}

		return lCodEsito;
	} // Chiude ExInserisciBeneficiCumuloWithoutSequence()
		// End MEV 26 Cumulo Step2

	/**
	 * ExInserisciBeneficioCumuloTipOrario
	 *
	 * @param BeneficioCumuloModel
	 *            aBenMod
	 * @param aTipologie
	 * @return BeneficioCumuloModel aBeneficio
	 * @throws F3BException
	 */

	public BeneficioCumuloModel ExInserisciBeneficioCumuloTipOrario(BeneficioCumuloModel aBenMod,
			ArrayList aTipologie, String[] aIdPenAcc, BeneficioCumuloModel aBenNMMod) throws F3BException {

		Connection lConn = null;

		BeneficioCumuloDAO lBenDao = null;
		TipologiaOrarioDAO lTipOrDao = null;
		PenaAccessoriaCumuloDAO lPenAccDao = null;
		BeneficioCumuloModel aBeneficio = new BeneficioCumuloModel(aBenMod);

		try {
			lConn = getDBTransaction();

			// inserimento benefici
			lBenDao = new BeneficioCumuloDAO(lConn);
			BigDecimal lKey = null;

			// indulto o amnistia
			if ("03".equals(aBenMod.getCodTipoBeneficio()) || "04".equals(aBenMod.getCodTipoBeneficio())) {
				lBenDao.setDAOFromModel(aBenMod);
				lKey = lBenDao.insert();
				aBeneficio.setIdBeneficioCumulo(lKey);
				lBenDao.stop();
			} else {
				// sospensione condizionale
				if (!(aBenNMMod != null && "-".equals(aBenMod.getCodSottotipoBeneficio()))) {
					lBenDao.setDAOFromModel(aBenMod);
					lKey = lBenDao.insert();
					aBeneficio.setIdBeneficioCumulo(lKey);
					lBenDao.stop();
				}

				// inserimento beneficio non menzione
				if (aBenNMMod != null) {
					lBenDao.setDAOFromModel(aBenNMMod);
					lBenDao.setBenIdBeneficioCumulo(lKey);
					BigDecimal lKeyNM = lBenDao.insert();
					aBenNMMod.setIdBeneficioCumulo(lKeyNM);
					lBenDao.stop();

					if (lKey == null)
						aBeneficio = aBenNMMod;
				}
			}

			lPenAccDao = new PenaAccessoriaCumuloDAO(lConn);
			if (aIdPenAcc != null && aIdPenAcc.length > 0) {
				for (int i = 0; i < aIdPenAcc.length; i++) {
					lPenAccDao.setBenIdBeneficioCumulo(lKey);
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
					// lTipOrDao.setBenIdBeneficio(lKey);
					lTipOrDao.setBenIdBeneficioCumulo(lKey);
					lTipOrDao.insert();
				}
			}

			lTipOrDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"BeneficioCumuloController.ExInserisciBeneficioCumuloTipOrario: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lBenDao);
			cleanup(lTipOrDao);
			cleanup(lPenAccDao);
			cleanup(lConn);
		}

		return aBeneficio;
	}

	/**
	 * ExModificaBeneficioCumuloTipologiaOrario
	 *
	 * @param BeneficioCumuloModel
	 *            aBeneficio
	 * @param aTipologie
	 * @return
	 * @throws F3BException
	 */
	public BeneficioCumuloModel ExModificaBeneficioCumuloTipologiaOrario(BeneficioCumuloModel aBeneficio,
			ArrayList aTipologie, String[] aIdPenAcc, BeneficioCumuloModel aBenNMMod) throws F3BException {

		Connection lConn = null;
		TipologiaOrarioDAO lTipOrDao = null;
		BeneficioCumuloDAO lBenDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		PenaAccessoriaCumuloDAO lPenAccDao = null;
		BeneficioCumuloModel lBenMod = new BeneficioCumuloModel(aBeneficio);

		try {
			// LogF3B.getLogger().debug("--XX--ExModificaBeneficioCumuloTipologiaOrario lBeneModel =
			// "+lBenMod);
			lConn = getDBTransaction();
			lBenDao = new BeneficioCumuloDAO(lConn);
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);

			// solo per NON_MENZIONE
			if (lBenMod != null && "02".equals(lBenMod.getCodTipoBeneficio())) {
				// cancello
				lBenDao.setCondizioneUpdate(lBenMod.getIdBeneficioCumulo());
				lBenDao.delete();
				lBenDao.stop();

				lBenSqlDao.ricercaBeneficioCumuloByBenIdBeneficioCum(lBenMod.getIdBeneficioCumulo());
				BeneficioCumuloModel lBenNNMMod = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();
				lBenSqlDao.stop();

				// cancello
				if (lBenNNMMod != null && lBenNNMMod.getIdBeneficioCumulo() != null) {
					lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficioCumulo());
					lBenDao.delete();
					lBenDao.stop();
				}

				// inserisco
				lBenDao.setDAOFromModel(aBenNMMod);
				BigDecimal lKey = lBenDao.insert();
				lBenDao.stop();
				aBenNMMod.setIdBeneficioCumulo(lKey);
			} else { // per tutti gli ALTRI CASI (SOSPENSIONE, INDULTO, ETC...)
				// Modifica il beneficio
				lBenDao.setDAOFromModelForUpdate(lBenMod);
				lBenDao.update();
				lBenDao.stop();

				// gestione eventuale non menzione
				lBenSqlDao.ricercaBeneficioCumuloByBenIdBeneficioCum(lBenMod.getIdBeneficioCumulo());
				BeneficioCumuloModel lBenNNMMod = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();

				if (lBenNNMMod != null && lBenNNMMod.getIdBeneficioCumulo() != null) {
					if (aBenNMMod == null) {
						// cancello
						lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficioCumulo());
						lBenDao.delete();
						lBenDao.stop();
					}
				} else if (aBenNMMod != null) {
					// inserisco
					lBenDao.setDAOFromModel(aBenNMMod);
					lBenDao.setBenIdBeneficioCumulo(lBenMod.getIdBeneficioCumulo());
					lBenDao.insert();
					lBenDao.stop();
				}

				// gestione pena accessoria
				// prima di aggiornare ripulisco il ben_id_benefecio_Cumulo delle pene_accessorie_Cumulo
				// in questione
				lPenAccDao = new PenaAccessoriaCumuloDAO(lConn);
				lPenAccDao.setBenIdBeneficioCumulo(null);
				lPenAccDao.selCondizioneUpdateBenIdBeneficioCumTitoloCum(lBenMod.getIdBeneficioCumulo(),
						lBenMod.getTitIdTitoloCumulato());
				lPenAccDao.update();
				lPenAccDao.stop();

				if (aIdPenAcc != null && aIdPenAcc.length > 0) {
					for (int i = 0; i < aIdPenAcc.length; i++) {
						lPenAccDao.setBenIdBeneficioCumulo(lBenMod.getIdBeneficioCumulo());
						lPenAccDao.selCondizioneUpdate(new BigDecimal(aIdPenAcc[i]));
						lPenAccDao.update();
					}
				}

				lPenAccDao.stop();

				// prima d'inserire la tipologia orario cancello i record presenti
				lTipOrDao = new TipologiaOrarioDAO(lConn);
				lTipOrDao.selCondizioneDeleteByIdBeneficioCumulo(lBenMod.getIdBeneficioCumulo());
				lTipOrDao.delete();
				lTipOrDao.stop();

				// inserimento tipologia Orario
				if (aTipologie != null) {
					Iterator iter = aTipologie.iterator();
					while (iter.hasNext()) {
						TipologiaOrarioModel TipoOrMod = (TipologiaOrarioModel) iter.next();
						lTipOrDao.setDAOFromModel(TipoOrMod);
						// lTipOrDao.setBenIdBeneficio(lBenMod.getIdBeneficioCumulo());
						lTipOrDao.setBenIdBeneficioCumulo(lBenMod.getIdBeneficioCumulo());
						lTipOrDao.insert();
					}
				}

				lTipOrDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"BeneficioCumuloController.ExModificaBeneficioCumuloTipologiaOrario: " + ex);
		} finally {
			cleanup(lTipOrDao);
			cleanup(lBenDao);
			cleanup(lPenAccDao);
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}

		return lBenMod;
	}

	/**
	 * ExCancellaBeneficioCumuloTipologiaOrario
	 *
	 * @param aBeneficio
	 * @throws F3BException
	 */

	public void ExCancellaBeneficioCumuloTipologiaOrario(BeneficioCumuloModel aBeneficio)
			throws F3BException {

		Connection lConn = null;
		BeneficioCumuloDAO lBenDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		TipologiaOrarioDAO lTipOrDao = null;
		PenaAccessoriaDAO lPenAccDao = null;
		PenaAccessoriaCumuloDAO lPenAccCumDao = null;

		// RichiestePmInCumuloSqlDAO lRichPmCumSqlDao = null;
		RichPMBeneficioCumSqlDAO lRichPmBenCumSqlDao = null;

		try {
			lConn = getDBTransaction();

			// 2025.11.18 - Correzione per ERR violazione FK
			if (aBeneficio.getFlagStato().compareTo("I") == 0) {
				// Devo verificare che il beneficio non sia puntato da una richiesta di revoca
				// altrimenti va in errore la FK
				lRichPmBenCumSqlDao = new RichPMBeneficioCumSqlDAO(lConn);

				RichPMBeneficioCumModel aModel = null;
				lRichPmBenCumSqlDao.ricercaRichPmBeneficioCumByIdBen(aBeneficio.getIdBeneficioCumulo());
				aModel = (RichPMBeneficioCumModel) lRichPmBenCumSqlDao.getModelByKey();
				if (aModel != null && aModel.getRicIdRichiestePmInCumulo() != null) {
					throw new F3BException(F3BException.USER_MESSAGE,
							"Il beneficio non è cancellabile in quanto collegato ad Richieste in cumulo."
									+ " Per procedere è necessario prima cancellare le richieste.");
				}
			}
			// 2025.11.18 - FINE

			// se esiste la tipologia orario e trattasi di Cancellazione Fisica: allora Delete
			if (aBeneficio.getFlagStato().compareTo("I") == 0) {
				lTipOrDao = new TipologiaOrarioDAO(lConn);
				lTipOrDao.selCondizioneDeleteByIdBeneficioCumulo(aBeneficio.getIdBeneficioCumulo());
				lTipOrDao.delete();
				lTipOrDao.stop();
			}

			// se esistono pene accessorie legate al beneficio, viene messo a null BEN_ID_BENEFICIO_CUMULO
			lPenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);
			lPenAccCumDao.setBenIdBeneficioCumulo(null);
			lPenAccCumDao.selCondizioneUpdateBenIdBeneficioCumTitoloCum(aBeneficio.getIdBeneficioCumulo(),
					aBeneficio.getTitIdTitoloCumulato());
			lPenAccCumDao.update();
			lPenAccCumDao.stop();

			// gestione non menzione
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lBenSqlDao.ricercaBeneficioCumuloByBenIdBeneficioCum(aBeneficio.getIdBeneficioCumulo());
			BeneficioCumuloModel lBenNNMMod = (BeneficioCumuloModel) lBenSqlDao.getModelByKey();

			if (lBenNNMMod != null && lBenNNMMod.getIdBeneficioCumulo() != null) {
				if (aBeneficio.getFlagStato().compareTo("I") == 0) {
					// Cancellazione Fisica
					lBenDao = new BeneficioCumuloDAO(lConn);
					lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficioCumulo());
					lBenDao.delete();
					lBenDao.stop();
				} else {
					// Cancellazione logica
					lBenDao = new BeneficioCumuloDAO(lConn);

					lBenNNMMod.setFlagStato("C");
					lBenNNMMod.setCodUfficioAggiornamento(aBeneficio.getCodUfficioAggiornamento());
					lBenNNMMod.setCodOperatoreAggiornamento(aBeneficio.getCodOperatoreAggiornamento());
					lBenNNMMod.setDataAggiornamento(aBeneficio.getDataAggiornamento());

					lBenDao.setDAOFromModelForUpdate(lBenNNMMod);
					lBenDao.setCondizioneUpdate(lBenNNMMod.getIdBeneficioCumulo());
					lBenDao.update();
					lBenDao.stop();
				}
			}

			// cancellazione beneficio: Fisica
			if (aBeneficio.getFlagStato().compareTo("I") == 0) {
				lBenDao = new BeneficioCumuloDAO(lConn);
				lBenDao.setCondizioneUpdate(aBeneficio.getIdBeneficioCumulo());
				lBenDao.delete();
				lBenDao.stop();
			} else {
				// Cancellazione logica
				lBenDao = new BeneficioCumuloDAO(lConn);
				aBeneficio.setFlagStato("C");
				lBenDao.setDAOFromModelForUpdate(aBeneficio);
				lBenDao.setCondizioneUpdate(aBeneficio.getIdBeneficioCumulo());
				lBenDao.update();
				lBenDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.info("DAOException: " + daoEx);
			throw new F3BException(
					"BeneficioCumuloController.ExCancellaBeneficioCumuloTipologiaOrario: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lBenDao);
			cleanup(lTipOrDao);
			cleanup(lPenAccDao);
			cleanup(lBenSqlDao);
			cleanup(lPenAccCumDao);
			cleanup(lConn);
		}
	}

	// MEV70
	public String ExRicercaBeneficioCumuloByTipoNaturaTitoloCum(BigDecimal aidTitolocum, String aCodNat,
			Vector<String> aCodTipoBen) throws F3BException {

		Connection lConn = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;

		String ltipoSosp = "";
		BeneficioCumuloModel lBenMod = null;
		// Vector<BeneficioCumuloModel> lVec = new Vector<BeneficioCumuloModel>();

		try {
			lConn = getDBConnection();
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lBenSqlDao.ricercaBeneficioCumuloByTitoloCum(aidTitolocum, aCodNat, aCodTipoBen);
			lBenSqlDao.start();
			while (lBenSqlDao.next()) {
				lBenMod = (BeneficioCumuloModel) lBenSqlDao.getModel();
				if (lBenMod != null && lBenMod.getIdBeneficioCumulo() != null) {

					if (lBenMod.getCodTipoBeneficio().equals("01")) {
						ltipoSosp += "S";
					} else if (lBenMod.getCodTipoBeneficio().equals("02")) {
						ltipoSosp += "M";
					}
				}
			}

		} catch (DAOException daoEx) {
			throw new F3BException(
					"BeneficioCumuloController.ExRicercaBeneficioCumuloByTipoNaturaTitoloCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lConn);
		}
		return ltipoSosp;
	}

}