package siap.siep.circostanza.util;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.circostanza.model.CircostanzaModel;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: CircostanzaUtil
 * </p>
 * <p>
 * Description: Classe Util per la creazione del vettore di circostanze
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
public class CircostanzaUtil {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public static Vector creaVectorCircostanze(Vector aVectCirc) throws Exception {
		// questo metodo mi sembra diventato inutile ...Paolo 09/04/2010
		// lo lascio x non asteriscarlo dappertutto

		// Questo metodo prende in ingresso un vettore di cirostanze e ne restituisce uno mettendo
		// come ultimo model quello contenete la circostanza avente Art. 442 e Codfonte 25 (C.P.P.)
		Vector lVect = new Vector();

		CircostanzaModel lCircModel = null;

		// ***********************************************************************************
		// Federica - a9-rr-078
		if (aVectCirc != null && aVectCirc.size() > 0) {
			for (int i = 0; i < aVectCirc.size(); i++) {
				CircostanzaModel lCirc = (CircostanzaModel) aVectCirc.get(i);
				lCircModel = lCirc;
			}
		}

		/*
		 * for (int i = 0; i < aVectCirc.size(); i++) { CircostanzaModel lCirc = (CircostanzaModel)
		 * aVectCirc.get(i); if (lCirc.getArticolo() == null || (lCirc.getArticolo() != null &&
		 * !lCirc.getArticolo().equals("442")) || ( lCirc.getArticolo() != null &&
		 * lCirc.getArticolo().equals("442") && (lCirc.getCodFonte() != null &&
		 * !lCirc.getCodFonte().equals("25"))) || (( lCirc.getArticolo() != null &&
		 * lCirc.getArticolo().equals("442") && (lCirc.getCodFonte() != null &&
		 * lCirc.getCodFonte().equals("25"))) && ( lCirc.getAnnoFonte() != null || lCirc.getNumeroFonte() !=
		 * null || lCirc.getComma() != null || lCirc.getNumero() != null || lCirc.getLettera() != null))) { //
		 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug("aggiunge a vettore non 442--->"+lCirc); lVect.add(lCirc); }
		 * else if ((lCirc.getArticolo() != null && lCirc.getArticolo().equals("442")) &&
		 * ((lCirc.getCodFonte() != null && lCirc.getCodFonte().equals("25") ) && (lCirc.getAnnoFonte() ==
		 * null && lCirc.getNumeroFonte() == null && lCirc.getComma() == null && lCirc.getNumero() == null &&
		 * lCirc.getLettera() == null))) { lCircModel = lCirc; // Art 44 Giudizio Abbreviato }
		 * 
		 * }
		 * 
		 */ // fine modifica ******************************************************************

		if (lCircModel != null) {
			lVect.add(lCircModel);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("LUNGHEZZA--->" + lVect.size());

		// questo metodo mi sembra diventato inutile ...Paolo 09/04/2010
		// riporto il vettore di ingresso e taglio la testa al toro
		// return lVect;
		return aVectCirc;
	}

}