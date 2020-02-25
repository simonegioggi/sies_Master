package siap.siep.modulocumulo.model;

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
public class ContinuazioneReatiCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8903633678492102746L;

	private String mDescrTipoContinuazione;
	private String mReati;

	private Vector mContinuazioni;

	public ContinuazioneReatiCumuloModel(Hashtable aTable) {
		if (aTable != null)
			getStringaReati(aTable);
	}

	public ContinuazioneReatiCumuloModel() {
		mDescrTipoContinuazione = "";
		mReati = "";
		setmContinuazioni(null);
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

	public Vector getmContinuazioni() {
		return mContinuazioni;
	}

	public void setmContinuazioni(Vector mContinuazioni) {
		this.mContinuazioni = mContinuazioni;
	}

}