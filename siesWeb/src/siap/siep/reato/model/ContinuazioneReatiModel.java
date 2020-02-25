/**
 * 
 */
package siap.siep.reato.model;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import f3b.model.GenericModel;

/**
 * @author Giselda De Vita
 *
 */
@SuppressWarnings("rawtypes")
public class ContinuazioneReatiModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 901707434719317087L;

	private String mDescrTipoContinuazione;
	private String mReati;

	private Vector mContinuazioni;

	public ContinuazioneReatiModel(Hashtable aTable) {
		if (aTable != null)
			getStringaReati(aTable);
	}

	public ContinuazioneReatiModel() {
		mDescrTipoContinuazione = "";
		mReati = "";
		setContinuazioni(null);
	}

	/**
	 * Da un'hashtable genera la Stringa dei Reati
	 * 
	 * @param continuazioni
	 */

	private void getStringaReati(Hashtable continuazioni) {

		String strCont = "";
		// String progReatoCont = "";
		String tipoCont = "";
		Collection coll = continuazioni.values();
		Iterator itxColl = coll.iterator();
		int conta = 0;

		Vector vectCont = new Vector();

		while (itxColl.hasNext()) {

			vectCont = (Vector) itxColl.next();
			Iterator itxVect = vectCont.iterator();
			conta = 0;

			while (itxVect.hasNext()) {

				strCont = (String) itxVect.next();
				String[] arrStr = strCont.split("@!");

				if (conta == 0) {
					if (strCont.equalsIgnoreCase("C2"))
						mDescrTipoContinuazione = "CONTINUAZIONE";
					else if (strCont.equalsIgnoreCase("C1"))
						mDescrTipoContinuazione = "CONCORSO FORMALE";

					tipoCont += mDescrTipoContinuazione + " tra i reati di cui ai nr. ";
				} else {
					tipoCont += arrStr[1] + " ";
					// progReatoCont = arrStr[0];
				}

				conta++;
			}

			// Fare un model per ciufolo!
		}
		mReati = tipoCont;
	}

	// Metodi Get e Set
	public String getDescrTipoContinuazione() {
		return mDescrTipoContinuazione;
	}

	public void setDescrTipoContinuazione(String aValore) {
		mDescrTipoContinuazione = aValore;
	}

	public String getReati() {
		return mReati;
	}

	public void setReati(String aValore) {
		mReati = aValore;
	}

	public Vector getContinuazioni() {
		return mContinuazioni;
	}

	public void setContinuazioni(Vector mContinuazioni) {
		this.mContinuazioni = mContinuazioni;
	}

}