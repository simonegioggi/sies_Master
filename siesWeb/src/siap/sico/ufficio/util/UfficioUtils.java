package siap.sico.ufficio.util;

import java.util.Iterator;
import java.util.Vector;

import siap.sico.ufficio.model.UfficioModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: UfficioUtils
 * </p>
 * <p>
 * Description: Classe di Utility varie riferite all'ufficio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UfficioUtils {

	/**
	 * Metodo che esegue l'estrazione da un insieme di uffici contenuti in un oggetto del tipo
	 * <code> Vector </code> di un determinato ufficio il quale codice viene passato come argomento. Se
	 * l'uffcio desiderato è contenuto nell'insieme, anch'esso passato come aragomento, l'oggetto
	 * <code>UfficioModel</code> corrispondente viene inserito in un nuovo insieme e ritornato. Il vettore
	 * ritornato da questo metodo può essere vuoto solo se l'uffcio desiderato non è presenete nella lista.
	 * <p>
	 * 
	 * @param aUffici
	 *            Vector Insime di uffci, dal quale estrarre quello interessato.
	 * @param aCodUfficio
	 *            String codice uffcio da ricercare
	 * @return Vector ritorna un elemento dell'uffcio trovato oppure se vuoto se non esiste nella lista.
	 */
	public static Vector estraeUfficio(Vector aUffici, String aCodUfficio) {
		Vector lVect = new Vector();
		Iterator lItx = aUffici.iterator();

		while (lItx.hasNext()) {
			UfficioModel lUfficio = (UfficioModel) lItx.next();
			if (lUfficio.getCodUfficio().equals(aCodUfficio)) {
				lVect.add(lUfficio);
				break;
			}
		}
		return lVect;
	}

	/**
	 * // 20171020: [EC] aggiungo metodo per individuare se l'ufficio ha o meno sezioni
	 * 
	 * @param aCodUfficio
	 * @return
	 */
	public static boolean isUfficioConSezioni(String aCodUfficio) {
		SezioneModel lSezMod = new SezioneModel();
		lSezMod.setCodUfficioAppartenenza(aCodUfficio);
		lSezMod.setCodice("");
		lSezMod.setDescrizione("");
		ISezione iSezione = null;
		Vector sezioni = new Vector();
		try {
			iSezione = SIGELookupRemote.getSezioneRemote();
			sezioni = iSezione.ExRicercaSezione(lSezMod);
		} catch (Exception ex) {
			sezioni = new Vector();
		}
		return sezioni.size() > 0;
	}

}