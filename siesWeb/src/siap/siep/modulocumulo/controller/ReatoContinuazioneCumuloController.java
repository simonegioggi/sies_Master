package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.modulocumulo.dao.ReatoCumuloDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.model.ContinuazioneReatiCumuloModel;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ReatoContinuazioneCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per la COntinuazione dei Reati_Cumulo
 * </p>
 * *
 * <p>
 * in ambito Cumulo (legato al titolo Cumulato)
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReatoContinuazioneCumuloController extends ReatoCumuloController implements IReatoCumulo {

	/**
	 * Cancellazione Continuazione Reati
	 * 
	 * @param aReato
	 * @throws F3BException
	 */

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public void ExCancellazioneContinuazioneReati(ReatoCumuloModel aReato) throws F3BException {
		Connection lConn = null;
		ReatoCumuloDAO lReaDao = null;
		ReatoCumuloModel lReaMod = new ReatoCumuloModel(aReato);

		try {

			lConn = getDBConnection();

			lReaDao = new ReatoCumuloDAO(lConn);

			lReaDao.setIdContinuazioneReatoCum(null);
			lReaDao.setTipoContinuazioneReato(null);

			lReaDao.setCondizione_CancellaContinuazione(lReaMod);

			lReaDao.update();
			lReaDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoContinuazioneCumuloController.ExCancellazioneContinuazioneReati: "
					+ ex);
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
		ReatoCumuloDAO lReaDao = null;
		ReatoCumuloModel lReaMod = null;

		try {
			lConn = getDBConnection();

			lReaDao = new ReatoCumuloDAO(lConn);

			if (aReati.size() > 0) {
				for (int i = 0; i < aReati.size(); i++) {
					lReaMod = (ReatoCumuloModel) aReati.get(i);

					if (i == 0) { // Si cancellano in primis tutte le Continuazioni sul titolo per
									// poi reinserirle
						lReaDao.setIdContinuazioneReatoCum(null);
						lReaDao.setTipoContinuazioneReato(null);

						ReatoCumuloModel lReaCanc = new ReatoCumuloModel();

						lReaCanc.setTitIdTitoloCumulato(lReaMod.getTitIdTitoloCumulato());

						lReaDao.setCondizione_Continuazioni(lReaCanc);

						lReaDao.update();
						lReaDao.stop();
					}

					// lReaDao.setFasSieIdFascicoloSiep(lReaMod.getFasSieIdFascicoloSiep());
					// lReaDao.setProgrReato(lReaMod.getProgrReato());
					lReaDao.setIdContinuazioneReatoCum(lReaMod.getIdContinuazioneReatoCum());
					lReaDao.setTipoContinuazioneReato(lReaMod.getTipoContinuazioneReato());

					lReaDao.setCondizione_Continuazioni(lReaMod);

					lReaDao.update();
					lReaDao.stop();
				}

				commit(lConn);
			}

		} catch (DAOException ex) {
			rollback(lConn);

			siesLogger.error("DAOException: ", ex);
			throw new F3BException("ReatoContinuazioneCumuloController.ExModificaContinuazioneReati: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ReatoContinuazioneCumuloController.ExModificaContinuazioneReati: " + ex);
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
		ReatoCumuloDAO lReaDao = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;

		try {

			lConn = getDBConnection();

			lReaDao = new ReatoCumuloDAO(lConn);
			lReaSqlDao = new ReatoCumuloSqlDAO(lConn);

			BigDecimal idContMax = lReaSqlDao.GetMaxIdContinuazione(idFas);

			return idContMax;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoContinuazioneCumuloController.ExGetMaxIdCondizione: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
	}

	/**
	 * Compone una stringa per ogni 'reato' comprendente tutte le fonti presenti nel reato. Quindi solo la
	 * sezione che descrive la norma violata.
	 * 
	 * @param inVect
	 *            <String> - Una Stringa per ogni reato (Progr_circ = 1)
	 * @return
	 */
	public Vector<String> getStringheReati(Vector inVect) {

		Vector outVect = new Vector();
		String strReato = new String("");
		boolean lFlagAnnoNumero;

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {
			ReatoCumuloModel lReato = (ReatoCumuloModel) itx.next();

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
	 * Teoricamente restituisce una HashTable avente per chiave l'idContinuazione e come valore un vettore di
	 * stringhe in cui il primo elemento è il TipoContinuazione e i successivi elementi sono stringhe che
	 * rappresentano il progressivo reato nel formato: progr@!progr o progr@!progrManuale
	 * 
	 * es: idContinuazione 1 - TipoCont - Progr 1@1 - Progr 3@3 idContinuazione 2 - TipoCont - Progr 2@2 -
	 * Progr 4@4
	 * 
	 * I reati con progressivi 1 e 3 sono in continuazionwe. I reati 2 e 4 sono in continuazione
	 *
	 * @param inVect
	 *            <ReatoCumuloModel> o <ReatoCircostanzaCumuloModel>
	 * @return Hashtable con le continuazioni [Key = idContinuazione, Value = Vector
	 *         <String>(tipoContinuazione,progr@!progr)]
	 */
	public Hashtable getTableContinuazioni(Vector inVect) {
		Hashtable table = new Hashtable();

		String str = new String("");
		BigDecimal idCont = null;
		String lProgressivo = new String("");
		ReatoCumuloModel lReato = new ReatoCumuloModel();
		ReatoCircostanzaCumuloModel lReatoCirc = new ReatoCircostanzaCumuloModel();

		Iterator itx = inVect.iterator();

		while (itx.hasNext()) {
			Object lObj = itx.next();
			if (lObj instanceof ReatoCumuloModel) {
				lReato = (ReatoCumuloModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaCumuloModel) {
				lReatoCirc = (ReatoCircostanzaCumuloModel) lObj;
				lReato = lReatoCirc.getReatoCum();
			}

			if (lReato.getProgrCircostanza().intValue() == 1) {
				if (lReato.getIdContinuazioneReatoCum() != null) {
					idCont = lReato.getIdContinuazioneReatoCum();

					lProgressivo = (lReato.getProgrNumeroManuale() != null) ? lReato.getProgrNumeroManuale()
							.toString() : lReato.getProgrReato().toString();

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

		return table;

	}

	/**
	 * getContinuazioniReati - restituisce un vector di ContinuazioniReatiCumuloModel le continuazioni
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

			ContinuazioneReatiCumuloModel lContinuazione = new ContinuazioneReatiCumuloModel();

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

			siesLogger.debug("> * Continuazione --->" + lTipoContinuazione + " " + lMessaggio);

			lContinuazioneReati.add(lContinuazione);
		}

		return lContinuazioneReati;
	}

}