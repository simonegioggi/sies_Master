package f3b.util;


/**
 * <p>Title: LookupClass </p>
 * <p>
 *  Description: Classe che si occupa di effettuare un class forname sulla
 *  classe passata come stringa al metodo di lookup.
 * </p>
 * <p>Copyright: Bull Italia S.p.A. Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
@SuppressWarnings("rawtypes")
public class LookupClass
{

	/**
	 * Metodo che effettua il lookup di una classe, non in dominio EJB.
	 * <p>
	 * 
	 * @param aClass
	 *            classe compresiva di package da chiamare.
	 * @return nuova istanza della classe.
	 * @throws F3BException
	 *             rilancia errore di eccezione.
	 */

	protected static Object lookup(String aClass) throws F3BException {

		Object lObj;

		try {
			Class lClass = Class.forName(aClass);
			lObj = lClass.newInstance();
		} catch (Exception ex) {
			throw new F3BException("Errore nella fase di lookup della classe : " + aClass);
		}

		return lObj;
	}

}