package siap.siep.reato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ContinuazioneReatiModel;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;

/**
 * <p>
 * Title: ReatoContinuazioneController
 * </p>
 * <p>
 * Description: Classe Controller per la COntinuazione dei Reati
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
public class ReatoContinuazioneController extends ReatoController implements IReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Cancellazione Continuazione Reati
	 *
	 * @param aReato
	 * @throws F3BException
	 */
	public void ExCancellazioneContinuazioneReati(ReatoModel aReato) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;
		ReatoModel lReaMod = new ReatoModel(aReato);

		try {
			lConn = getDBConnection();

			lReaDao = new ReatoDAO(lConn);

			lReaDao.setIdContinuazioneReato(null);
			lReaDao.setTipoContinuazioneReato(null);

			lReaDao.setCondizione_CancellaContinuazione(lReaMod);

			lReaDao.update();
			lReaDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExModificaContinuazioneReati: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
	}

	/**
	 * ExModificaContinuazioneReati
	 *
	 * @param aReati
	 * @throws F3BException
	 */
	public void ExModificaContinuazioneReati(ArrayList aReati) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;
		ReatoModel lReaMod = null;

		try {

			lConn = getDBConnection();

			lReaDao = new ReatoDAO(lConn);

			if (aReati.size() > 0) {

				for (int i = 0; i < aReati.size(); i++) {

					lReaMod = new ReatoModel();
					lReaMod = (ReatoModel) aReati.get(i);

					if (i == 0) {

						lReaDao.setIdContinuazioneReato(null);
						lReaDao.setTipoContinuazioneReato(null);

						ReatoModel lReaCanc = new ReatoModel();
						lReaCanc.setFasSieIdFascicoloSiep(lReaMod.getFasSieIdFascicoloSiep());
						lReaDao.setCondizione_Continuazioni(lReaCanc);

						lReaDao.update();
						lReaDao.stop();
					}

					// lReaDao.setFasSieIdFascicoloSiep(lReaMod.getFasSieIdFascicoloSiep());
					// lReaDao.setProgrReato(lReaMod.getProgrReato());
					lReaDao.setIdContinuazioneReato(lReaMod.getIdContinuazioneReato());
					lReaDao.setTipoContinuazioneReato(lReaMod.getTipoContinuazioneReato());

					lReaDao.setCondizione_Continuazioni(lReaMod);

					lReaDao.update();
					lReaDao.stop();
				}

				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExModificaContinuazioneReati: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
	}

	/**
	 * ExGetMaxIdContinuazione - Get Max Id Continuazione
	 *
	 * @param idFas
	 * @return Max Id COntinuazione
	 * @throws F3BException
	 */
	public BigDecimal ExGetMaxIdContinuazione(BigDecimal idFas) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDao = null;

		try {

			lConn = getDBConnection();

			lReaDao = new ReatoDAO(lConn);
			lReaSqlDao = new ReatoSqlDAO(lConn);

			BigDecimal idContMax = lReaSqlDao.GetMaxIdContinuazione(idFas);

			return idContMax;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExGetMaxIdCondizione: " + ex);
		} finally {
			cleanup(lReaDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lReaSqlDao);
			cleanup(lConn);
		}
	}

	public Vector getStringheReati(Vector inVect) {

		Vector outVect = new Vector();
		String strReato = new String("");
		boolean lFlagAnnoNumero;

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = (ReatoModel) itx.next();

			if (lReato.getProgrCircostanza().intValue() == 1) {

				if (strReato.length() != 0) {

					outVect.addElement(strReato);
					strReato = "";
				}
			}

			lFlagAnnoNumero = false;
			if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("")
					&& lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("")) {
				lFlagAnnoNumero = true;
			}

			if (lFlagAnnoNumero) {
				if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
						&& !lReato.getDescrFonte().equals("-"))
					strReato += lReato.getDescrFonte() + " ";
				if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals(""))
					strReato += lReato.getAnnoFonte();
				if (lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
					strReato += "/" + lReato.getNumeroFonte();
			}

			if (lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
				strReato += " art." + lReato.getArticolo();
			if (lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("")
					&& !lReato.getDescrSottonumerazione().equals("-"))
				strReato += " " + lReato.getDescrSottonumerazione();

			if (!lFlagAnnoNumero) {
				if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
						&& !lReato.getDescrFonte().equals("-"))
					strReato += " " + lReato.getDescrFonte();
			}

			if (lReato.getComma() != null && !lReato.getComma().equals(""))
				strReato += " c. " + lReato.getComma();
			// ***********************************************************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			if (lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("")
					&& !lReato.getDescrCommaQualificante().equals("-"))
				strReato += " " + lReato.getDescrCommaQualificante();
			// ***********************************************************************************
			if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
				strReato += " l. " + lReato.getLettera();
			if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
				strReato += " n. " + lReato.getNumero();

			strReato += ", ";
		}

		outVect.addElement(strReato);

		return outVect;
	}

	/**
	 * getTableContinuazioni - calcola le continuazioni
	 *
	 * @param inVect
	 * @return Hashtable con le continuazioni
	 */
	public Hashtable getTableContinuazioni(Vector inVect) {

		Hashtable table = new Hashtable();
		String str = new String("");
		BigDecimal idCont = null;
		String lProgressivo = new String("");
		ReatoModel lReato = new ReatoModel();
		ReatoCircostanzaModel lReatoCirc = new ReatoCircostanzaModel();

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			// ReatoModel lReato = (ReatoModel)itx.next();

			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			if (lReato.getProgrCircostanza().intValue() == 1) {

				if (lReato.getIdContinuazioneReato() != null) {

					idCont = lReato.getIdContinuazioneReato();
					lProgressivo = (lReato.getProgrNumeroManuale() != null)
							? lReato.getProgrNumeroManuale().toString()
							: lReato.getProgrReato().toString();
					str = lReato.getProgrReato().toString() + "@!" + lProgressivo;

					Vector vect = new Vector();

					if (table.containsKey(idCont)) {

						vect = (Vector) table.get(idCont);
						vect.add(str);
						table.remove(idCont);
					} else {

						vect.add(lReato.getTipoContinuazioneReato());
						vect.add(str);
					}

					table.put(idCont, vect);
				}
			}
		}

		/*
		 * if (table.size() > 0) {
		 *
		 * String strRet = ""; Collection coll = table.values(); itx = coll.iterator();
		 *
		 * Vector vect2 = new Vector();
		 *
		 * while (itx.hasNext()) {
		 *
		 * vect2 = (Vector)itx.next();
		 *
		 * Iterator itx2 = vect2.iterator();
		 *
		 * while (itx2.hasNext()) {
		 *
		 * str = (String)itx.next(); strRet += str } } }
		 */

		return table;
	}

	/**
	 * getContinuazioniReati - restituisce un vector di ContinuazioniReatiModel le continuazioni
	 *
	 * @param inVect
	 * @return Hashtable con le continuazioni
	 */
	public Vector getContinuazioniReati(Hashtable continuazioni) {

		String strCont = "";
		// String progReatoCont = "";
		// String tipoCont = "";
		Collection coll = continuazioni.values();
		Iterator itxColl = coll.iterator();
		int conta = 0;

		Vector vectCont = new Vector();
		Vector lContinuazioneReati = new Vector();

		// Ciclo sulle Continuazioni dei reati
		while (itxColl.hasNext()) {

			ContinuazioneReatiModel lContinuazione = new ContinuazioneReatiModel();

			vectCont = (Vector) itxColl.next();
			Iterator itxVect = vectCont.iterator();
			conta = 0;
			String lTipoContinuazione = "";
			String lMessaggio = "";

			while (itxVect.hasNext()) {

				strCont = (String) itxVect.next();
				String[] arrStr = strCont.split("@!");

				if (conta == 0) {
					if (strCont.equalsIgnoreCase("C2"))
						lTipoContinuazione = "Continuazione";
					else if (strCont.equalsIgnoreCase("C1"))
						lTipoContinuazione = "Concorso Formale";

					// lMessaggio += lTipoContinuazione + " tra i reati di cui ai nr. ";
				} else {
					lMessaggio += arrStr[1] + " ";
					// progReatoCont = arrStr[0];
				}

				conta++;
			}

			lContinuazione.setDescrTipoContinuazione(lTipoContinuazione);
			lContinuazione.setReati(lMessaggio);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("> * Continuazione --->" + lTipoContinuazione + " " + lMessaggio);

			lContinuazioneReati.add(lContinuazione);
		}

		return lContinuazioneReati;
	}

}