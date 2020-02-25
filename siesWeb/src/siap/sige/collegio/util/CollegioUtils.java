package siap.sige.collegio.util;

import java.util.ArrayList;
import java.util.Collection;

import siap.sico.decodifiche.model.DecodificheModel;

/**
 * Classe Utilità per la Gestione Collegi.
 * <p>
 * 
 * @author user
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class CollegioUtils {

	public static Collection getElencoCodiciCollegi() {
		Collection lColl = new ArrayList();
		for (int i = 1; i <= 30; i++) {
			DecodificheModel lDecMod = new DecodificheModel();
			lDecMod.setCode("" + i);
			lDecMod.setDescription("" + i);
			lColl.add(lDecMod);
		}
		return lColl;
	}

	public static boolean isValidSize(Object aObj[], int aNum) {
		if (aObj != null && aObj.length > aNum)
			return true;

		return false;
	}

}