package siap.sico.decodifiche.controller;

import java.util.Collection;
import java.util.Iterator;

import siap.controller.SiapController;
import f3b.model.DecodeModel;

@SuppressWarnings("rawtypes")
public class ComboManager extends SiapController {

	private Collection mCollection;

	/**
	 * Inizializzazione degli attributi del Singleton
	 */
	public ComboManager() {
	}

	public ComboManager(Collection aCollection) {
		mCollection = aCollection;
	}

	/**
	 * Da una collection di DecodeModel viene restituita una String con i valori da inserire nel javascript
	 * 
	 * @return
	 */
	public String getStringArrayValue() {
		String lStringArray = new String();

		if (mCollection != null) {
			Iterator lItx = mCollection.iterator();

			while (lItx.hasNext()) {
				DecodeModel lMod = (DecodeModel) lItx.next();
				lStringArray += "\"" + lMod.getDescription() + "\",";
			}

		}

		lStringArray = lStringArray.substring(0, lStringArray.length() - 1);

		return lStringArray;
	}

	/**
	 * Da un acollection di DecodeModel viene restituita una Map
	 * 
	 * @param aCollection
	 * @return
	 */
	public String getStringArrayCode() {
		String lStringArray = new String("");

		if (mCollection != null) {
			Iterator lItx = mCollection.iterator();

			while (lItx.hasNext()) {
				DecodeModel lMod = (DecodeModel) lItx.next();
				lStringArray += "\"" + lMod.getCode() + "\",";
			}

		}

		lStringArray = lStringArray.substring(0, lStringArray.length() - 1);

		return lStringArray;
	}

	/**
	 * Da una collection di DecodeModel viene restituita una String con i valori da inserire nel javascript
	 * 
	 * @return
	 */
	public String getListForAutocompleter() {
		String lStringArray = "[";

		if (mCollection != null) {
			Iterator lItx = mCollection.iterator();

			while (lItx.hasNext()) {

				DecodeModel lMod = (DecodeModel) lItx.next();
				lStringArray += "[\"" + lMod.getDescription() + "\"," + "\"" + lMod.getDescription() + "\"]";
				lStringArray += ",";
			}

		}

		lStringArray = lStringArray.substring(0, lStringArray.length() - 1);
		lStringArray += "]";

		return lStringArray;
	}

}