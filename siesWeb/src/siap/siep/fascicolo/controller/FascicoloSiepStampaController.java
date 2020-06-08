package siap.siep.fascicolo.controller;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.sico.evento.model.XModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteMagistratoSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.SIAPStampaController;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.alias.dao.AliasSqlDAO;
import siap.siep.alias.model.AliasModel;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.dao.IstruttoriaSqlDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.statoesecuzione.controller.StatoEsecuzioneController;
import siap.util.SIESSwitch;

/**
 * <p>
 * Title: FascicoloSiepControllerModel
 * </p>
 * <p>
 * Description: Realizza il controller del Fascicolo Siep Stampa
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
public class FascicoloSiepStampaController extends SIAPStampaController implements IFascicoloSiepStampa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * prelevaDatiStampaFascicolo
	 *
	 * @param aFascMod
	 * @param aUtenteMod
	 * @return TreeModel
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiStampaFascicolo(FascicoloSiepModel aFascMod, UtenteModel aUtenteMod)
			throws F3BException {

		Connection lConn = null;
		TreeModel lTreeRoot = null;

		try {
			lConn = getDBConnection();
			lTreeRoot = this.prelevaDatiFascicolo(aFascMod, aUtenteMod, lConn, true);
		} finally {
			cleanup(lConn);
		}
		return lTreeRoot;
	}

	/**
	 * Crea la root del Documento
	 *
	 * @param aFascMod
	 * @param aUtenteMod
	 * @return XModel
	 */
	public XModel createRootFascicolo(FascicoloSiepModel aFascModel, UtenteModel aUtenteModel)
			throws F3BException {

		XModel lStampa = new XModel();

		String descrTipoUff = aFascModel.getDescrTipoUfficio().toUpperCase();
		lStampa.setUfficio(aFascModel.getDescrComuneUfficio().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff.toUpperCase());
		lStampa.setDataElaborazione(DateUtils.getSysDate());

		if (aUtenteModel != null && aUtenteModel.getUfficioUtente() != null) {
			UfficioModel lUffMod = aUtenteModel.getUfficioUtente();

			lStampa.setCap(lUffMod.getCap());
			lStampa.setFax(lUffMod.getFax());
			lStampa.setIndirizzo(lUffMod.getIndirizzo());
			lStampa.setTelefono(lUffMod.getTelefono());
		}

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
		}

		if (descrTipoUff.indexOf("GENERALE") > 0) {// GDV modifica
													// lStampa.setFirmatario("Il Sostituto Procuratore
													// Generale");
			lStampa.setFirmatario("Il Procuratore Generale");
		} else {
			lStampa.setFirmatario("Il Pubblico Ministero");
		}

		return lStampa;
	}

	/**
	 * Metodo che estrae i dati del fascicolo e crea il treemodel corretto
	 *
	 * @param aFascMod
	 * @param aUtenteMod
	 * @return
	 * @throws F3BException
	 */
	private TreeModel prelevaDatiFascicolo(FascicoloSiepModel aFascMod, UtenteModel aUtenteMod,
			Connection lConn, boolean aInserisciEvento) throws F3BException {

		TreeModel lTreeRoot = new TreeModel();

		SoggettoSqlDAO lSogDao = null;
		ResidenzaSqlDAO lResDao = null;
		ReatoSqlDAO lReaDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		MagistratoCompetenteMagistratoSqlDAO lMagSql = null;
		AliasSqlDAO lAliasSqlDAO = null;

		try {

			lTreeRoot = new TreeModel(createRootFascicolo(aFascMod, aUtenteMod));
			lTreeRoot.add(new TreeModel(aUtenteMod));

			// Fascicolo
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(aFascMod.getIdFascicoloSiep());
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

			TreeModel lTreeFasMod = new TreeModel(lFasModel);

			// Soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lFasModel.getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogDao.getModelByKey();
			TreeModel lTreeSogMod = new TreeModel(lSogModel);

			// Alias
			lAliasSqlDAO = new AliasSqlDAO(lConn);
			lAliasSqlDAO.ricercaAliasByIdSoggetto(lFasModel.getSogIdSoggetto());
			Vector lAlias = new Vector(lAliasSqlDAO.getModels());

			// Residenza
			lResDao = new ResidenzaSqlDAO(lConn);
			lResDao.ricercaResidenzaByFascicoloXStampa(lFasModel.getIdFascicoloSiep());
			Vector lResidenze = new Vector(lResDao.getModels());

			Iterator lItx = null;

			// Aggiungere Residenze e Domicili
			if (lResidenze != null) {
				lItx = lResidenze.iterator();
				while (lItx.hasNext()) {
					ResidenzaModel lResModel = new ResidenzaModel((ResidenzaModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lResModel));
				}
			}
			// Aggiungere Alias
			if (lAlias != null) {
				lItx = lAlias.iterator();
				while (lItx.hasNext()) {
					AliasModel lAliasModel = new AliasModel((AliasModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lAliasModel));
				}
			}

			appendTableToFascicoloSiep(lConn, lFasModel.getIdFascicoloSiep(), lTreeFasMod,
					aFascMod.getFlagAltraCausa());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Prima di appendere lo Stato di Esecuzione = = = "
					+ SIESSwitch.isReworkStatoEsecuzioneOn());
			if (aInserisciEvento) {
				if (SIESSwitch.isReworkStatoEsecuzioneOn()) {
					// Se sono nel rework dello stato esecuzione chiamo il nuovo
					// controller che gestisce lo stato esec.

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("Prima di appendere lo Stato di Esecuzione");
					StatoEsecuzioneController lStat = new StatoEsecuzioneController();
					lStat.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lFasModel.getIdFascicoloSiep());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("Ho appeso lo Stato di Esecuzione");

				} else
					this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn,
							lFasModel.getIdFascicoloSiep());

			}
			// ---Prima--- this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn,
			// lFasModel.getIdFascicoloSiep());

			// Avvocati
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lFasModel.getIdFascicoloSiep());
			Vector lAvvocati = new Vector(lAvvDao.getModels());

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

			// Magistrato competente
			lMagSql = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			lMagSql.ricercaMagistratoCompetenteByFascicolo(lFasModel.getIdFascicoloSiep());
			MagistratoCompetenteMagistratoModel lMagModel = (MagistratoCompetenteMagistratoModel) lMagSql
					.getModelByKey();

			MagistratoModel lMag = null;
			if (lMagModel != null) {
				lMag = new MagistratoModel(lMagModel.getMagistrato());
			}

			if (lMag != null) {
				TreeModel lTreeMag = new TreeModel(lMag);
				lTreeFasMod.add(lTreeMag);
			}

			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));
		} catch (DAOException daoEx) {
			throw new F3BException("FascicoloSiepStampaController.prelevaDatiStampaFascicolo: " + daoEx);
		} finally {
			cleanup(lSogDao);
			cleanup(lResDao);
			cleanup(lReaDao);
			cleanup(lFasDao); // sca
			cleanup(lAvvDao);
			cleanup(lMagSql);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAliasSqlDAO);
		}
		return lTreeRoot;
	}

	/**
	 * prelevaDatiStampaFascicoliMultipli - Metodo per la stampa di copertine multiple
	 *
	 * @param aFascMod
	 * @param aUtenteMod
	 * @return TreeModel
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiStampaFascicoliMultipli(FascicoloSiepModel aFascMod, UtenteModel aUtenteMod)
			throws F3BException {

		IstruttoriaSqlDAO lIstrDao = null;
		Connection lConn = null;
		TreeModel lTreeRoot = null;

		lTreeRoot = new TreeModel(createRootFascicolo(aFascMod, aUtenteMod));
		lTreeRoot.add(new TreeModel(aUtenteMod));

		try {
			lConn = getDBConnection();

			lIstrDao = new IstruttoriaSqlDAO(lConn);
			lIstrDao.ricercaIdFascicoli(aFascMod);
			Vector lFascicoli = new Vector(lIstrDao.getModels());

			Iterator lItx = lFascicoli.iterator();

			if (lFascicoli.size() == 0)
				throw new F3BException("Nessun procedimento nell'intervallo impostato!");

			while (lItx.hasNext()) {
				FascicoloSiepModel lFascicolo = (FascicoloSiepModel) lItx.next();
				TreeModel lTree = this.prelevaDatiFascicolo(lFascicolo, aUtenteMod, lConn, false);
				lTreeRoot.add(lTree);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSiepStampaController.prelevaDatiStampaFascicolo: " + daoEx);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lIstrDao);
			cleanup(lConn);
		}
		return lTreeRoot;

	}

}