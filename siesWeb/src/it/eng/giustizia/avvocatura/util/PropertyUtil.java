package it.eng.giustizia.avvocatura.util;

import java.util.List;

/**
 * Fornisce metodi di controllo sulle proprietà di un oggetto generico
 *
 * @author EC
 * @version 1.0
 */
public class PropertyUtil {

	/**
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * Verifica la presenza di un int
	 *
	 * @param i
	 * @return
	 */
	public static boolean isPresent(int i) {
		return isPresent(i, 0);
	}
	/**
	 * 
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
	@SuppressWarnings("rawtypes")
	public static boolean isPresent(List l) {
		return l != null && !l.isEmpty();
	}

}