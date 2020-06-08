package siap.sico.ufficio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.ufficio.dao.UfficioDAO;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficiProvvedimentoModel;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UfficioController extends SiapController implements IUfficio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector ExGetUfficio() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lDao = null;

		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lDao = new UfficioSqlDAO(lConn);
			lDao.start();
			while (lDao.next()) {
				lUfficio.add(lDao.getModel());
			}
			lDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException(
					"UfficioController.ExGetUfficio: Non posso leggere gli Uffici : " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaTipoUfficiDescr() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaTipoUfficiDescr();
			lUDao.start();

			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				// lUmod = new UfficioModel((UfficioModel)lUDao.getModel() );
				// Add al vettore
				// lUfficio.add( lUmod.getDescrTipoUfficio() );
				String descUff = lUDao.getString("DESCR");
				String codUff = lUDao.getString("COD_UFFICIO");
				lUfficio.add(new DecodeModel(codUff, descUff));
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiDescr: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaUfficiAccorpati(String tipoUfficio, String ufficioCompetente) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiAccorpati(tipoUfficio, ufficioCompetente);
			lUDao.start();

			UfficioAccorpatoModel lUmod = new UfficioAccorpatoModel();
			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioAccorpatoModel((UfficioAccorpatoModel) lUDao.getUfficioAccorpatoModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiAccorpati: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaDistretti() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaDistretti();
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaDistretti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaTDSM() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaTDSM();
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaDistretti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaPMM() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaPMM();
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaDistretti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ExGetListaComuniUfficiPerDistretto(String aDistretto, String aComune, String aCodUfficio)
			throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();

			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiPerDistretto(aDistretto, aComune, aCodUfficio);
			lUDao.start();

			UfficioModel lUmod = null;

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException("ComuneController.ExGetListaComuniUfficiPerDistretto: " + daoex);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public String getDescTipoUffByCodUfficio(String aCodTipoUfficio) throws F3BException {

		String lDescr = "";
		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.getDescTipoUffByCodUfficio(aCodTipoUfficio);
			lUDao.start();
			while (lUDao.next())
				lDescr = lUDao.getString("DESC_UFFICIO");
			lUDao.stop();
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.getDescTipoUffByCodUfficio : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}

		return lDescr;
	}

	public UfficioModel getUfficioByCodTipoUffDescrComune(String aCodTipoUfficio, String aDescrComune)
			throws F3BException {

		Connection lConn = null;

		UfficioSqlDAO lUDao = null;
		UfficioModel lUffMod = null;

		try {
			lConn = getDBConnection();

			lUDao = new UfficioSqlDAO(lConn);

			lUDao.selUfficioByCodTipoUffDescrComune(aCodTipoUfficio, aDescrComune);
			lUffMod = (UfficioModel) lUDao.getModelByKey();

			if (lUffMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "Ufficio inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException(
					"UfficioController.getCodUfficioByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}

		return lUffMod;
	}

	public UfficioModel ExRicercaSedeUfficioEmittenteByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUffDao = null;
		UfficioModel lUffMod;
		try {
			lConn = getDBConnection();
			lUffDao = new UfficioSqlDAO(lConn);
			lUffDao.ricercaSedeUfficioEmittenteByFascicolo(aFascicolo);

			lUffMod = (UfficioModel) lUffDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UfficioController.ExRicercaSedeUfficioEmittenteByFascicolo: Non posso leggere + DAOex: "
							+ daoEx);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}
		return lUffMod;
	}

	public UfficioModel ExRicercaSedeByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUffDao = null;
		UfficioModel lUffMod;
		try {
			lConn = getDBConnection();
			lUffDao = new UfficioSqlDAO(lConn);
			lUffDao.ricercaTDSByFascicolo(aFascicolo);
			lUffMod = (UfficioModel) lUffDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UfficioController.ExRicercaSedeByFascicolo: Non posso leggere + DAOex: " + daoEx);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}
		return lUffMod;
	}

	public Vector ListaUfficiPerTipo(String aCodTipoUfficio) throws F3BException {

		return ListaUfficiPerTipo(aCodTipoUfficio, null);
	}

	/**
	 * Lista Uffici per Tipo Ufficio
	 *
	 * @param aCodTipoUfficio
	 * @return lUfficio
	 * @throws F3BException
	 */
	public Vector ListaUfficiPerTipo(String aCodTipoUfficio, String aFlagAccorp) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiByCodTipoUfficio(aCodTipoUfficio, aFlagAccorp);
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());

				// Add al vettore
				lUfficio.add(lUmod);
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiPerTipo: " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	/**
	 * Lista Uffici per Tipo Ufficio utile nella funzione del cumulo
	 *
	 * @param aCodTipoUfficio
	 * @return lUfficio
	 * @throws F3BException
	 */
	public Vector ListaUfficiPerTipoUfficiCumulo(String aCodTipoUfficio) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiByCodTipoUfficioUfficiCumulo(aCodTipoUfficio);
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());

				// Add al vettore
				lUfficio.add(lUmod);
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiPerTipo: " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaUfficiDistretto(String CodDistretto) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiDistretto(CodDistretto);
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiDistretto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaUfficiMinorDistretto(String CodDistretto) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiMinorDistretto(CodDistretto);
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiDistretto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public boolean verifyUfficioByDescrComune(String aDescrComune) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		boolean aUfficio = false;

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.getCodUfficioByDescrComune(aDescrComune);
			lUDao.start();

			if (lUDao.next()) {
				// L'eventuale codice lo carico nella variabile da restituire
				aUfficio = true;
			}
			lUDao.stop();

			if (!aUfficio)
				throw new SICOException(SICOException.USER_MESSAGE, "Procura inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException(
					"UfficioController.verifyUfficioByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return aUfficio;
	}

	/**
	 * Ricerca Ufficio by Key
	 *
	 * @param aCodiceUfficio
	 * @return lUffMod
	 * @throws F3BException
	 */
	public UfficioModel getUfficioByKey(String aCodiceUfficio) throws F3BException {

		Connection lConn = null;

		UfficioSqlDAO lUDao = null;
		UfficioModel lUffMod = null;

		try {
			lConn = getDBConnection();

			lUDao = new UfficioSqlDAO(lConn);

			lUDao.selUfficioByCod(aCodiceUfficio);
			lUffMod = (UfficioModel) lUDao.getModelByKey();

			if (lUffMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "Ufficio inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException(
					"UfficioController.getCodUfficioByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}

		return lUffMod;
	}

	/**
	 * Lista Uffici di Sorveglianza Nazionali
	 *
	 * @return lUfficio
	 * @throws F3BException
	 */
	public Vector ListaUDS() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUDS();
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUDS: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	/**
	 * Lista Uffici di Sorveglianza Nazionali
	 *
	 * @return lUfficio
	 * @throws F3BException
	 */
	public Vector ListaUDSM() throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUDSM();
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUDS: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaUfficiCompletaDistretto(String CodDistretto) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiCompletaDistretto(CodDistretto);
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiDistretto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	public Vector ListaUfficiCompletaDistrettoAbilitatiLogin(String CodDistretto) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiCompletaDistrettoAbilitatiLogin(CodDistretto);
			lUDao.start();

			UfficioModel lUmod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioModel lUffMod = new UfficioModel();
			// ---Stub 6/2/2002 UfficioController lUffCtrl= new UfficioController();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				lUfficio.add(lUmod);
				// fine while
			}
			lUDao.stop();

			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiDistretto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	/**
	 * La funzione utilizza la ricerca della funzione ListaUfficiCompletaDistrettoAbilitatiLogin ma ne filtra
	 * la lista risultato per tipo di ufficio PGCAP, PM, TDS, UDS. fornendo così l'elenco delle "Procure
	 * Generali Presso la Corte di Appello" (PGCAP) e delle "Procure presso il Tribunale Ordinario" (PM), gli
	 * Uffici di Sorveglianza e i Tribunali di Sorveglianza.di uno stesso distretto. La lista viene filtrata
	 * anche per i seguenti uffici SIGE: GIP, GIPM, DIBM, CAS, CAPSM, DIB, CAP, TRIBSD, CASAP.
	 *
	 * @param CodDistretto
	 * @return
	 * @throws F3BException
	 */
	public Vector ListaUfficiProcuraXDistrettoAbilitatiLogin(String CodDistretto) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.listaUfficiCompletaDistrettoAbilitatiLogin(CodDistretto);

			// lUfficio = new Vector(lUDao.getModels());

			lUDao.start();
			UfficioModel lUmod = new UfficioModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficioModel((UfficioModel) lUDao.getModel());
				// Add al vettore
				if (lUmod.getCodTipoUfficio().compareToIgnoreCase("PGCAP") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("PM") == 0
						// Modifica del 19/04/2016 abilitato ufficio PMM
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("PMM") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("TDS") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("UDS") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("UDSM") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("TDSM") == 0 ||
						// MEV 15 - Revisione SIGE - abilitazione uffici SIGE
						lUmod.getCodTipoUfficio().compareToIgnoreCase("GIP") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("GIPM") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("DIBM") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("CAS") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("CAPSM") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("DIB") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("CAP") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("TRIBSD") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("CASAP") == 0
						// MERGE v10 COLLAUDO: AGGIUNGO 2 CONTROLLI
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("GUP") == 0
						|| lUmod.getCodTipoUfficio().compareToIgnoreCase("GUPM") == 0) {
					lUfficio.add(lUmod);
				}
			}
			lUDao.stop();
			if (lUfficio.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.ListaUfficiDistretto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	/**
	 * Metodo di ricerca del TDS a cui afferisce un UDS collegato oppure dell'UDS relativo al TDS collegato. I
	 * parametri passati sono relativi all'ufficio connesso, tranne aCodTipoUfficio, relativo al tipo ufficio
	 * desiderato.
	 *
	 * @param aCodTipoUfficio
	 * @param aCodDistretto
	 * @param aCodComune
	 * @return lUffMod
	 * @throws F3BException
	 */
	public UfficioModel getUfficioUDSTDS(String aCodDistretto, String aCodTipoUfficio, String aCodComune)
			throws F3BException {

		Connection lConn = null;

		UfficioSqlDAO lUDao = null;
		UfficioModel lUffMod = null;

		try {
			lConn = getDBConnection();

			lUDao = new UfficioSqlDAO(lConn);

			lUDao.getUfficioUDSTDS(aCodDistretto, aCodTipoUfficio, aCodComune);
			lUffMod = (UfficioModel) lUDao.getModelByKey();

			if (lUffMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "Ufficio inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.getCodUfficioUDSTDS : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}

		return lUffMod;
	}

	public String getPrefissoUtenteUfficio(String aCodDistretto) throws F3BException {

		String lPrefix = "";
		Connection lConn = null;
		UfficioSqlDAO lUDao = null;

		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.getPrefissoUtenteUfficioLogin(aCodDistretto);
			lUDao.start();
			while (lUDao.next())
				lPrefix = lUDao.getString("RV_ABBREVIATION");
			lUDao.stop();
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.getPrefissoUtenteUfficio : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}

		return lPrefix;
	}

	/**
	 * Lista Uffici Interessati ad un trasferimento Ordinanza/Decreto
	 *
	 * @param aIdFascicoloSiep
	 * @param aNumFascUnificati
	 * @return lUfficio
	 * @throws F3BException
	 */
	public Vector ListaUfficiInteressatiProvvedimento(BigDecimal aIdFascicoloSius,
			BigDecimal aNumFascUnificati, String aCodOggettoProc) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		Vector lUfficio = new Vector();
		// MEV_39 03/01/2018
		FascMsToFascSiepSqlDAO lFascMsToSiepDao = null;
		FascMsToFascSiepModel lFascMsModel = null;
		FascicoloSiepSqlDAO lFasSiepDao = null;
		FascicoloSiepModel lFasSiepModel = null;

		try {
			lConn = getDBConnection();

			// MEV_39 03/01/2018
			lFascMsToSiepDao = new FascMsToFascSiepSqlDAO(lConn);
			lFasSiepDao = new FascicoloSiepSqlDAO(lConn);

			lUDao = new UfficioSqlDAO(lConn);
			lUDao.getUfficiInteressatiProvvedimento(aIdFascicoloSius, aNumFascUnificati);
			lUDao.start();

			UfficiProvvedimentoModel lUmod = new UfficiProvvedimentoModel();

			while (lUDao.next()) {
				// Decodifica
				lUmod = new UfficiProvvedimentoModel(
						(UfficiProvvedimentoModel) lUDao.getUfficiProvvedimentoModel());

				// Add al vettore
				lUfficio.add(lUmod);

				// MEV_39 03/01/2018 ***** inizio *****
				// Solo in fase di trasmissione da parte dell'ufficio TDS,
				// tra le Autorità Destinatarie oltre all'Ufficio della Procura presso il Tribunale
				// 'proprietario' del titolo esecutivo di classe IV viene inserito anche
				// l'Ufficio della Procura presso il Tribunale 'proprietario' del titolo esecutivo di classe I
				if (aCodOggettoProc != null
						&& ("C036".equals(aCodOggettoProc) || "C029".equals(aCodOggettoProc)
								|| "U077".equals(aCodOggettoProc) || "U082".equals(aCodOggettoProc))
						// intervento post collaudo 13.3 (terza sessione) per risolvere anomalia 4
						|| "U023".equals(aCodOggettoProc)) {

					if (lUmod.getFasSiepOrigine() != null && !lUmod.getFasSiepOrigine().equals("")) {
						lFascMsModel = new FascMsToFascSiepModel();
						lFascMsToSiepDao.ricercaFascSiepColl(new BigDecimal(lUmod.getFasSiepOrigine()));
						lFascMsToSiepDao.start();
						lFascMsModel = (FascMsToFascSiepModel) lFascMsToSiepDao.getModelByKey();

						if (lFascMsModel != null && lFascMsModel.getFasSieIdFascicoloSiep() != null) {
							lFasSiepDao.ricercaFascicoloByKey(lFascMsModel.getFasSieIdFascicoloSiep());
							lFasSiepDao.start();
							lFasSiepModel = new FascicoloSiepModel();
							lFasSiepModel = (FascicoloSiepModel) lFasSiepDao.getModelByKey();
						}

						if (lFasSiepModel != null && !"".equals(lFasSiepModel.toString())) {
							lUmod = new UfficiProvvedimentoModel(lFasSiepModel.getChiaveUfficio(),
									lFasSiepModel.getCodTipoUfficio(), lFasSiepModel.getDescrTipoUfficio(),
									lFasSiepModel.getCodDistretto(), null,
									lFasSiepModel.getDescrComuneUfficio(),
									lFasSiepModel.getChiaveAnno().toString(),
									lFasSiepModel.getChiaveProgr().toString(),
									// null,
									null);
							lUfficio.add(lUmod);
						}
					}
				}
				// MEV_39 03/01/2018 ***** fine *****
			}
			lUDao.stop();
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.listaUfficiInteressati: " + daoEx);
		} finally {
			cleanup(lUDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFascMsToSiepDao);
			cleanup(lFasSiepDao);
			cleanup(lConn);
		}
		return lUfficio;
	}

	/**
	 * Verifica se 2 Codici Ufficio appartengono allo stesso distretto.
	 *
	 * @param aCodUfficio1
	 * @param aCodUfficio2
	 * @return aCond
	 * @throws F3BException
	 */
	public boolean stressoDistretto(BigDecimal aCodUfficio1, BigDecimal aCodUfficio2, Connection aConn)
			throws F3BException {

		UfficioSqlDAO lUDao = null;
		boolean aCond = false;

		try {
			lUDao = new UfficioSqlDAO(aConn);
			lUDao.stessoDistretto(aCodUfficio1, aCodUfficio2);
			lUDao.start();

			if (lUDao.next()) {
				aCond = true;
			}
			lUDao.stop();
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.stessoDistretto: " + daoEx);
		} finally {
			cleanup(lUDao);
		}
		return aCond;
	}

	public UfficioModel ExModificaUfficio(UfficioModel aUfficio) throws F3BException {

		Connection lConn = null;
		UfficioDAO lUffDao = null;
		UfficioModel lUffMod = new UfficioModel(aUfficio);

		try {
			lConn = getDBConnection();
			lUffDao = new UfficioDAO(lConn);
			lUffDao.setDAOFromModelForUpdate(aUfficio);
			lUffDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("UfficioController.ExModifica: Non posso modificare: " + ex);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}
		return lUffMod;
	}

	public UfficioModel ExRicercaUfficioByCod(String aCod) throws F3BException {

		Connection lConn = null;
		UfficioSqlDAO lUffDao = null;
		UfficioModel lUffMod = null;

		try {
			lConn = getDBConnection();
			lUffDao = new UfficioSqlDAO(lConn);
			lUffDao.selUfficioByCod(aCod);
			lUffMod = (UfficioModel) lUffDao.getModelByKey();
		} catch (Exception daoEx) {
			throw new F3BException("UfficioController.ExRicercaUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}
		if (lUffMod == null)
			throw new F3BException("Ufficio mancante! ");

		return lUffMod;
	}

	public UfficioModel getUfficioAccorpatoByAccorpanteIncrement(String aCodUfficioAccorpante,
			String aIncremento) throws F3BException {

		Connection lConn = null;

		UfficioSqlDAO lUffSqlDao = null;
		UfficioModel lUffMod = null;

		try {
			lConn = getDBConnection();

			lUffSqlDao = new UfficioSqlDAO(lConn);

			lUffSqlDao.getUfficioAccorpatoByCodUffAccorpanteIncrement(aCodUfficioAccorpante, aIncremento);

			lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new SICOException(
					"UfficioController.getUfficioAccorpatoByAccorpanteIncrement: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new SICOException(
					"UfficioController.getUfficioAccorpatoByAccorpanteIncrement: Non posso leggere  : " + ex);
		} finally {
			cleanup(lUffSqlDao);
			cleanup(lConn);
		}

		return lUffMod;
	}

	/**
	 *
	 * @param aCodTipoUfficio
	 * @param aCodComune
	 * @return
	 * @throws F3BException
	 */
	public UfficioModel getUfficioByCodTipoUffCodComune(String aCodTipoUfficio, String aCodComune)
			throws F3BException {

		Connection lConn = null;

		UfficioSqlDAO lUffSqlDao = null;
		UfficioModel lUffMod = null;

		try {
			lConn = getDBConnection();

			lUffSqlDao = new UfficioSqlDAO(lConn);
			lUffSqlDao.selUfficioByCodTipoUffCodComune(aCodTipoUfficio, aCodComune);

			lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
			lUffSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new SICOException(
					"UfficioController.getUfficioByCodTipoUffCodComune: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new SICOException(
					"UfficioController.getUfficioByCodTipoUffCodComune: Non posso leggere  : " + ex);
		} finally {
			cleanup(lUffSqlDao);
			cleanup(lConn);
		}

		return lUffMod;
	}

	/**
	 * Il metodo restituisce la tipologia di ufficio codice ufficio
	 *
	 * @param aCodDistretto
	 * @return
	 * @throws F3BException
	 */
	public String getTipoUfficioUtente(String aCodDistretto) throws F3BException {

		// 13/03/2018 metodo introdotto per anomalia m_dg.DOG07.28-02-2018.0007015.U (parametro scadenziario
		// mancante)
		// in fase di associazioni di un utente ad un ufficio andiamo a controllare se è stato inserito in
		// tabella PARAMETRO
		// il record per singolo ufficio per lo scadenziario di 'TERMINE SOTTOSCRIZIONE VERBALE M.A.
		String lPrefix = "";
		Connection lConn = null;
		UfficioSqlDAO lUDao = null;
		try {
			lConn = getDBConnection();
			lUDao = new UfficioSqlDAO(lConn);
			lUDao.getTipoUfficioUtente(aCodDistretto);
			lUDao.start();
			while (lUDao.next())
				lPrefix = lUDao.getString("RV_LOW_VALUE");
			lUDao.stop();
		} catch (DAOException daoEx) {
			throw new SICOException("UfficioController.getTipoUfficioUtente : " + daoEx);
		} finally {
			cleanup(lUDao);
			cleanup(lConn);
		}

		return lPrefix;
	}

	// MEV_39: aggiunti metodi di ricerca
	public UfficiProvvedimentoModel getUfficioPMEsecDest(BigDecimal idFascicoloSiepOrigine,
			String codTipoUfficio, String chiaveUfficioSiepOrigine) throws F3BException {

		Connection c = null;
		UfficioSqlDAO usDAO = null;
		UfficiProvvedimentoModel upm = null;

		try {
			c = getDBConnection();
			usDAO = new UfficioSqlDAO(c);
			usDAO.getUfficioPMEsecDest(idFascicoloSiepOrigine, codTipoUfficio, chiaveUfficioSiepOrigine);
			usDAO.start();
			if (usDAO.next())
				upm = (UfficiProvvedimentoModel) usDAO.getUfficiProvvedimentoModel();
			usDAO.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SICOException("UfficioController.getUfficioPMEsecDest: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new SICOException("UfficioController.getUfficioPMEsecDest: Non posso leggere  : " + ex);
		} finally {
			cleanup(usDAO);
			cleanup(c);
		}
		// valore di ritorno
		return upm;
	}

	public UfficiProvvedimentoModel getUfficioPGCAPEsecDest(String codTipoUfficio,
			String chiaveUfficioSiepOrigine) throws F3BException {

		Connection c = null;
		UfficioSqlDAO usDAO = null;
		UfficiProvvedimentoModel upm = null;

		try {
			c = getDBConnection();
			usDAO = new UfficioSqlDAO(c);
			usDAO.getUfficioPGCAPEsecDest(codTipoUfficio, chiaveUfficioSiepOrigine);
			usDAO.start();
			if (usDAO.next())
				upm = (UfficiProvvedimentoModel) usDAO.getUfficiProvvedimentoModel();
			usDAO.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SICOException("UfficioController.getUfficioPGCAPEsecDest: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new SICOException("UfficioController.getUfficioPGCAPEsecDest: Non posso leggere  : " + ex);
		} finally {
			cleanup(usDAO);
			cleanup(c);
		}
		// valore di ritorno
		return upm;
	}

}