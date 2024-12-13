package f3b.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.impl.dv.util.Base64;

import f3b.security.SecurityException;
import siap.sico.decodifiche.model.DecodificheModel;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Utils - Classe di utilità generica.
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Utils {

	/**
	 * Metodo che ritorna una stringa criptata con algoritimo SHA-1, passando come parametro la stringa da
	 * criptare.
	 *
	 * @param aPassword
	 *            stringa da criptare.
	 * @return la stringa criptata.
	 */
	public static String cryptPassword(String aPassword) throws SecurityException {

		MessageDigest lMd = null;
		String lPassword = (aPassword != null) ? aPassword : "";
		String lPasswordCrpt = null;

		try {
			lMd = MessageDigest.getInstance("SHA-1");
			// 20250923: Chiusura PKI SHA1 deprecata --> NO
			// lMd = MessageDigest.getInstance("SHA-256");
			// se cambiamo istanza vanno rigenerate tutte le password!!!
			lPasswordCrpt = new String(Base64.encode(lMd.digest(lPassword.getBytes())));
		} catch (Exception ex) {
			throw new SecurityException("Errore durante il crypting della password");
		}

		return lPasswordCrpt;
	}

	/**
	 * Metodo che ritorna una stringa criptata passando come parametro la stringa da criptare.
	 *
	 * @param aPassword
	 *            stringa da criptare.
	 * @return la stringa criptata.
	 */
	public static String pwdNSCEncode(String password) throws SecurityException {

		BASE64Encoder encoder = new BASE64Encoder();
		String encodedBytes = null;
		try {
			encodedBytes = encoder.encodeBuffer(password.getBytes());
		} catch (Exception ex) {
			throw new SecurityException("Errore durante il pwdNSCEncode della password");
		}
		return encodedBytes;
	}

	/**
	 * Metodo che ritorna una stringa decriptata passando come parametro la stringa da decriptare.
	 *
	 * @param aPassword
	 *            stringa da decriptare.
	 * @return la stringa decriptata.
	 */
	public static String pwdNSCDecode(String password) throws SecurityException {

		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decodedBytes = null;
		try {
			decodedBytes = decoder.decodeBuffer(password);
		} catch (Exception ex) {
			throw new SecurityException("Errore durante il pwdNSCDecode della password");
		}
		return new String(decodedBytes);
	}

	//
	// Vector Handler
	//

	/**
	 * Ritorna un sottoinsieme di elementi da un <code>Vector</code>, per un determinato range.<br>
	 * Il metodo accetta come parametri il vettore da elaborare, inizio e fine come range.
	 *
	 * @param aVector
	 *            insieme di elementi da dove estrarre.
	 * @param aStart
	 *            da inizio elemento.
	 * @param aEnd
	 *            a fine elemento.
	 * @return il sottoinsieme di elementi.
	 */
	public static Vector subVector(Vector aVector, int aStart, int aEnd) {

		Vector lReturn = new Vector();

		List lList = null;
		Iterator lItx = null;

		if (aVector == null || (aStart == aEnd) || (aEnd < aStart))
			return lReturn;

		lList = aVector.subList(aStart, aEnd);
		lItx = lList.iterator();

		while (lItx.hasNext())
			lReturn.add(lItx.next());

		return lReturn;
	}

	/**
	 * Ritorna in una unica stringa tutti gli elementi contenuti in un vettore, opportunamente delimitati dal
	 * separatore passato come parametro.
	 *
	 * @param aVector
	 *            insieme di elementi.
	 * @param aSeparator
	 *            carattere di separazione.
	 * @return la stringa con gli elementi del vettore.
	 */
	public static String vectorToString(Vector aVector, String aSeparator) {

		if (aVector == null)
			return null;

		aSeparator = (aSeparator == null ? ";" : aSeparator);

		int lSize = aVector.size();
		String lReturn = "";

		for (int i = 0; i < lSize; i++) {
			lReturn += aVector.get(i);
			lReturn += aSeparator;
		}

		return (lReturn.substring(0, lReturn.length() - 1));
	}

	/**
	 * Ritorna in una unica stringa tutti gli elementi contenuti in un vettore, opportunamente delimitati dal
	 * separatore passato come parametro.
	 *
	 * @param aVector
	 *            insieme di elementi.
	 * @param aSeparator
	 *            carattere di separazione.
	 * @return la stringa con gli elementi del vettore.
	 */
	public static String arrayToString(Object[] aObjects, String aSeparator) {

		if (aObjects == null)
			return null;

		aSeparator = (aSeparator == null ? ";" : aSeparator);

		int lSize = aObjects.length;
		String lReturn = "";

		for (int i = 0; i < lSize; i++) {
			lReturn += aObjects[i];
			lReturn += aSeparator;
		}

		return (lReturn.substring(0, lReturn.length() - aSeparator.length()));
	}

	/**
	 * Questo metodo acccetta in ingresso una Hashtable e ritorna un oggetto Iterator che contiene la lista
	 * delle chiavi sortate.
	 *
	 * @param aHash
	 *            insieme di elementi.
	 * @return l'iteratore.
	 */
	public static Iterator sortHashKeys(Hashtable aHash) {

		// Affinchè possa effetturae il sort della tabella di hash
		// e necessario ottenere da quest'ultimo un oggetto derivato dalla
		// classe List.
		// Tale passaggio avviene invocando il metodo asList della classe
		// Arrays quale accetta in ingresso la lista delle chiavi della tabella
		// di hash ed invocando il metodo toArray si ottiene un array di oggetti Object[]
		// /"hash.keySet().toArray()/".
		List lList = Arrays.asList(aHash.keySet().toArray());

		// Invoco il metodo sort della classe Collections, al quale passando
		// l'oggetto List effettua il sort.
		Collections.sort(lList);

		// Una volta sortato ritorna l'oggetto iterator.
		Iterator itx = lList.iterator();

		return itx;
	}

	/**
	 * STUB : PM - Ma questo non è uguale al precedente ?
	 * Questo metodo acccetta in ingresso una Hashtable e ritorna un oggetto Iterator che contiene la lista
	 * delle chiavi sortate.
	 *
	 * @param aHash
	 *            insieme di elementi.
	 * @return l'iteratore.
	 */
	public static Iterator sortHashKeysForBLOB(Hashtable aHash) {

		// Affinchè possa effetturae il sort della tabella di hash
		// e necessario ottenere da quest'ultimo un oggetto derivato dalla
		// classe List.
		// Tale passaggio avviene invocando il metodo asList della classe
		// Arrays quale accetta in ingresso la lista delle chiavi della tabella
		// di hash ed invocando il metodo toArray si ottiene un array di oggetti Object[]
		// /"hash.keySet().toArray()/".
		List lList = Arrays.asList(aHash.keySet().toArray());

		// Invoco il metodo sort della classe Collections, al quale passando
		// l'oggetto List effettua il sort.
		Collections.sort(lList);

		// Una volta sortato ritorna l'oggetto iterator.
		Iterator itx = lList.iterator();

		return itx;
	}

	/**
	 * Verifica se la stringa passata è un valore numerico.
	 *
	 * @param lValue
	 *            Stringa da controllare.
	 * @return lo stato validazione.
	 */
	public static boolean isValidNumber(String lValue) {

		if (lValue == null || lValue.trim().length() == 0)
			return false;

		try {
			new BigDecimal(lValue.replace(',', '.'));
		} catch (NumberFormatException nex) {
			return false;
		}

		return true;
	}

	/**
	 * Metodo che verifica se un oggetto e nullo.
	 *
	 * @param aObj
	 *            oggetto da verificare.
	 * @return l'esito della verifica.
	 */
	public static boolean isNullObj(Object aObj) {

		if (aObj == null)
			return true;
		else
			return false;
	}

	/**
	 * Converte una stringa in valuta euro.
	 *
	 * @param aValue
	 *            valore da convertire.
	 * @return valore in valuta Euro.
	 */
	public static BigDecimal toEuro(String aValue) {

		try {
			BigDecimal aBdVal = new BigDecimal(aValue);
			aBdVal = (new BigDecimal(aBdVal.longValue() / 1936.27));
			aBdVal = aBdVal.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP);
			return aBdVal;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Elimina gli elementi dalla collection mappati nell'array
	 *
	 * @param coll
	 * @param mask
	 */
	public static void negativeFilter(Collection coll, String[] mask) {

		Iterator<DecodificheModel> iter = coll.iterator();
		while (iter.hasNext()) {
			DecodificheModel dm = iter.next();
			boolean remove = false;
			for (String xx : mask) {
				if (xx.equals(dm.getCode())) {
					remove = true;
				}
			}
			if (remove) {
				iter.remove();
			}
		}
	}

	/**
	 * Ottiene la descrizione del campo contenuto nella collection
	 *
	 * @param coll
	 * @param code
	 */
	public static String getDescItem(Collection coll, String code) {

		Iterator<DecodificheModel> iter = coll.iterator();
		String ret = "";
		while (iter.hasNext()) {
			DecodificheModel dm = iter.next();
			if (dm.getCode().equals(code)) {
				ret = dm.getDescription();
			}
		}
		return ret;
	}

	// MEV10-s3 + MEV 16: aggiunti metodi di utilities
	/**
	 * Verifica la presenza di una Stringa
	 *
	 * @param s
	 *            la Stringa da verificare
	 * @param trim
	 *            true se vogliamo effettuare il trim sulla stringa
	 * @return true se è presente, altrimenti false
	 */
	public static boolean isPresent(String s, boolean trim) {

		if (trim)
			return (s != null && s.trim().length() > 0);
		else
			return (s != null && s.length() > 0);
	}

	/**
	 * Verifica la presenza di una Stringa
	 *
	 * @param s
	 *            la Stringa da verificare
	 * @return true se è presente, altrimenti false
	 */
	public static boolean isPresent(String s) {

		return isPresent(s, false);
	}

	/**
	 * Confronta due String
	 *
	 * @param a
	 * @param b
	 * @param trim
	 * @return
	 */
	public static boolean areEquals(String a, String b, boolean trim) {

		String s = a == null ? "" : trim ? a.trim() : a;
		String t = b == null ? "" : trim ? b.trim() : b;

		return s.equals(t);
	}

	/**
	 * Confronta due Object
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean match(Object source, Object target) {

		/*
		 * Source Target null null true null "" true null <obj> false "" null true "" "" true "" <obj> false
		 * <obj> null false <obj> "" false <obj1> <obj2> <obj1>.equals(<obj2>), where <obj1> is not null
		 */
		return ((source == target)) || ((source == null) && ("".equals(target)))
				|| (("".equals(source)) && (target == null)) || ((source != null) && (source.equals(target)));
	}

	/**
	 * Confronta due String
	 *
	 * @param a
	 * @param b
	 * @param trim
	 * @return
	 */
	public static boolean areEquals(String a, String b) {

		return areEquals(a, b, false);
	}

	/**
	 * Verifica la presenza di un Object(Date, Integer, Double, etc.) verificando semplicemente che non sia
	 * <code>null</code>
	 *
	 * @param o
	 *            l'Object da verificare
	 * @return true se è presente, altrimenti false
	 */
	public static boolean isPresent(Object o) {

		return o != null;
	}

	/**
	 * Verifica la presenza di un Object[] verificando semplicemente che non sia <code>null</code>
	 *
	 * @param o
	 *            l'array di Object da verificare
	 * @return true se è presente, altrimenti false
	 */
	public static boolean isPresent(Object[] o) {

		return o != null && o.length > 0;
	}

	/**
	 * Verifica la presenza di un int
	 *
	 * @param i
	 * @return
	 */
	public static boolean isPresent(int i) {

		return isPresent(i, 0);
	}

	/**
	 * Verifica la presenza di un int
	 *
	 * @param i
	 * @param controller
	 *            valore con cui verificare la presenza
	 * @return
	 */
	public static boolean isPresent(int i, int controller) {

		return (i != controller);
	}

	/**
	 * Verifica la presenza di un List
	 *
	 * @param l
	 * @return
	 */
	public static boolean isPresent(List l) {

		return l != null && !l.isEmpty();
	}

	/**
	 * Verifica la presenza di un BigDecimal
	 *
	 * @param o
	 * @return boolean
	 */
	public static boolean isPresent(BigDecimal o) {

		return o != null && !"0".equals(o.toString());
	}

	/**
	 * 20190226: [SG] aggiunto metodo di controllo Verifica la presenza di una Stringa, che non sia un
	 * trattino
	 *
	 * @param s
	 *            la Stringa da verificare
	 * @return true se è presente, altrimenti false
	 */
	public static boolean isPresentNotTrattino(String s) {

		return isPresent(s, false) && !"-".equals(s);
	}

}