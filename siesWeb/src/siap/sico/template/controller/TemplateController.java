package siap.sico.template.controller;

import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.template.dao.TemplateDAO;
import siap.sico.template.dao.TemplateSqlDAO;
import siap.sico.template.model.TemplateModel;
import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TemplateController
 * </p>
 * <p>
 * Description: Classe Controller per Template
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
public class TemplateController extends SiapController implements ITemplate {

	public Vector ExRicercaAllTemplate() throws F3BException {
		Connection lConn = null;
		Vector lTemplati = new Vector();
		TemplateSqlDAO lTemDao = null;
		try {
			lConn = getDBConnection();
			lTemDao = new TemplateSqlDAO(lConn);
			lTemplati = new Vector(lTemDao.ricercaAll());

			if (lTemplati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("TemplateController.ExRicercaTemplate: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTemDao);
			cleanup(lConn);
		}
		return lTemplati;

	}

	public Vector ExRicercaTemplate(TemplateModel aTemplate) throws F3BException {
		Connection lConn = null;
		Vector lTemplati = new Vector();
		TemplateSqlDAO lTemDao = null;

		try {
			lConn = getDBConnection();
			lTemDao = new TemplateSqlDAO(lConn);
			lTemDao.ricercaTemplate(aTemplate);
			lTemplati = new Vector(lTemDao.getModels());
			if (lTemplati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("TemplateController.ExRicercaTemplate: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTemDao);
			cleanup(lConn);
		}
		return lTemplati;
	}

	public TemplateModel ExRicercaTemplateByKey(String aKey) throws F3BException {
		Connection lConn = null;
		TemplateDAO lTemDao = null;
		TemplateModel lTemMod;

		try {
			lConn = getDBConnection();
			lTemDao = new TemplateDAO(lConn);
			lTemDao.setCondizioneById(aKey);
			lTemMod = (TemplateModel) lTemDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("TemplateController.ExRicercaTemplate: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTemDao);
			cleanup(lConn);
		}
		return lTemMod;
	}

	public TemplateModel ExRicercaTemplateByCodMotivo(String aKey) throws F3BException {
		Connection lConn = null;
		TemplateSqlDAO lTemDao = null;
		TemplateModel lTemMod;

		try {
			lConn = getDBConnection();
			lTemDao = new TemplateSqlDAO(lConn);
			lTemDao.ricercaTemplateByCodMotivo(aKey);
			lTemMod = (TemplateModel) lTemDao.getModelByKey();
			if (lTemMod == null) {
				throw new F3BException(F3BException.USER_MESSAGE, "Template Inesistente");
			}

		} catch (DAOException daoEx) {
			throw new F3BException("TemplateController.ExRicercaTemplate: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTemDao);
			cleanup(lConn);
		}
		return lTemMod;
	}

	public TemplateModel ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(String aTipoEvento,
			String aTipoProv, String aCodMotivo, String aFlagTemplate) throws F3BException {
		Connection lConn = null;

		TemplateSqlDAO lTemDao = null;
		TemplateModel lTemMod;

		try {
			lConn = getDBConnection();

			lTemDao = new TemplateSqlDAO(lConn);

			lTemDao.ricercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(aTipoEvento, aTipoProv, aCodMotivo,
					aFlagTemplate);
			lTemMod = (TemplateModel) lTemDao.getModelByKey();

			if (lTemMod == null) {
				throw new F3BException(F3BException.USER_MESSAGE, "Template Inesistente");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("TemplateController.ExRicercaTemplate: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTemDao);
			cleanup(lConn);
		}

		return lTemMod;
	}

	/*
	 * Ricerca di Template generica con risultato Vector di DecodeModel. Utilizzato per la creazione di una
	 * ComboBox.
	 */

	public Vector ExListaCbxTemplate(TemplateModel aTemplate) throws F3BException {
		Connection lConn = null;
		Vector lTemplati = new Vector();

		TemplateSqlDAO lTemDao = null;

		try {
			lConn = getDBConnection();
			lTemDao = new TemplateSqlDAO(lConn);
			lTemDao.ricercaTemplate(aTemplate);

			lTemDao.start();
			while (lTemDao.next()) {
				String lCod = ((TemplateModel) lTemDao.getModel()).getIdTemplate();
				String lDescrizione = ((TemplateModel) lTemDao.getModel()).getDescr();

				lTemplati.add(new DecodeModel(lCod, lDescrizione));
			}

			lTemDao.stop();
			/*
			 * if ( lTemplati.size() == 0 ) { throw new
			 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato"); }
			 */
		} catch (DAOException daoEx) {
			throw new F3BException("TemplateController.ExListaCbxTemplate: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTemDao);
			cleanup(lConn);
		}
		return lTemplati;
	}

}