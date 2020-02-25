package f3b.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <p>
 * Title: GenericModel.java
 * </p>
 * <p>
 * Description: Superclasse model. Questa classe deve essere ereditata da tutti i model utilizzati nel
 * progetto.
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class GenericModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 887363307892170384L;

	private String mMessage = null;

	/**
	 * Costruttore di classe.
	 */
	public GenericModel() {
	}

	/**
	 * Ritorna il contenuto del messaggio impostato.
	 * <p>
	 * 
	 * @return il contenuto dell'attributo di classe.
	 */
	public String getMessage() {
		return mMessage;
	}

	/**
	 * Imposta un messggio nell'attributo di classe.
	 * <p>
	 * 
	 * @param aValue
	 *            messaggio.
	 */
	public void setMessage(String aValue) {
		mMessage = aValue;
	}

	@SuppressWarnings("rawtypes")
	public String toString() {

		String Out = "";
		String[] lArg = {};
		Out = this.getClass().getName() + "\n";
		Method[] lFieldMetho = this.getClass().getDeclaredMethods();

		int lCounthMethod = lFieldMetho.length; // conta dei metodi
		try {
			for (int y = 0; y < lCounthMethod; y++) // for
			{
				String lAttributeName = lFieldMetho[y].getName(); // nome metodo
				Class[] lParameter = lFieldMetho[y].getParameterTypes(); // parametri del metodo

				if (lParameter.length == 0) {
					if (lAttributeName.startsWith("get", 0)) { // metodo di get senza parametri
						Object lRet = lFieldMetho[y].invoke(this, (Object[]) lArg); // invochi il get

						if (lRet == null) {
							Out += lAttributeName.substring(3, lAttributeName.length()) + " = null \n";
						} else if (lRet instanceof Calendar) {
							// Out += lRet.toString() + "\n";
							GregorianCalendar lCal = new GregorianCalendar();
							lCal = (GregorianCalendar) lRet;

							Out += lAttributeName.substring(3, lAttributeName.length()) + " = "
									+ lCal.get(Calendar.DAY_OF_MONTH) + "/" + lCal.get(Calendar.MONTH) + "/"
									+ lCal.get(Calendar.YEAR) + "\n";
						} else {
							Out += lAttributeName.substring(3, lAttributeName.length()) + " = "
									+ lRet.toString() + "\n";
						}
						// fai quello che devi fare controllando che cosa restituisce il metodo...
					}
				}
			} // fine for
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Out;
	}

}