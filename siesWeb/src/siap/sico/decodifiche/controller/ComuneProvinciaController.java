package siap.sico.decodifiche.controller;

import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.decodifiche.dao.ComuneSqlDAO;
import siap.sico.decodifiche.model.ComuneProvinciaModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComuneProvinciaController extends SiapController implements IComuneProvincia {

	public Vector ExListaProv() throws F3BException {
		Connection lConn = null;
		ComuneSqlDAO lCPDao = null;
		Vector lProv = new Vector();

		try {
			lConn = getDBConnection();
			lCPDao = new ComuneSqlDAO(lConn);
			lCPDao.listaProvince();
			lCPDao.start();

			ComuneProvinciaModel lCPmod = new ComuneProvinciaModel();
			// ---Stub 6/2/2002 ComuneModel lComMod = new ComuneModel();
			// ---Stub 6/2/2002 ComuneController lComCtrl= new ComuneController();

			while (lCPDao.next()) {
				// Decodifica
				lCPmod = new ComuneProvinciaModel((ComuneProvinciaModel) lCPDao.getModel());
				// Add al vettore
				lProv.add(lCPmod);
				// fine while
			}

			if (lProv.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("ComuneProvinciaController.ExListaProv: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCPDao);
			cleanup(lConn);
		}

		return lProv;
	}

	public Vector ExListaProvPerTipoUfficio(String codTipoUfficio) throws F3BException {
		Connection lConn = null;
		ComuneSqlDAO lCPDao = null;
		Vector lProv = new Vector();

		try {
			lConn = getDBConnection();
			lCPDao = new ComuneSqlDAO(lConn);
			lCPDao.listaProvincePerTipoUfficio(codTipoUfficio);
			lCPDao.start();

			ComuneProvinciaModel lCPmod = new ComuneProvinciaModel();

			while (lCPDao.next()) {
				// Decodifica
				lCPmod = new ComuneProvinciaModel((ComuneProvinciaModel) lCPDao.getModel());
				// Add al vettore
				lProv.add(lCPmod);
				// fine while
			}

			if (lProv.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("ComuneProvinciaController.ExListaProv: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCPDao);
			cleanup(lConn);
		}

		return lProv;
	}

}