package siap.sico.ufficio.controller;

import f3b.util.F3BException;
import siap.sico.ufficio.model.UfficioModel;

/**
 * <p>
 * Title: UfficioUtils
 * </p>
 * <p>
 * Description: Classe di utilità per l'Ufficio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class UfficioUtils {

	public UfficioUtils() {
	}

	public static UfficioModel getUfficioByCodUfficio(String aCodUfficio) throws F3BException {

		UfficioModel lUfficio = new UfficioModel();
		UfficioController lUff = new UfficioController();
		lUfficio = lUff.getUfficioByKey(aCodUfficio);
		return lUfficio;
	}

	public static String getDescTipoUffByCodUfficio(String aCodTipoUfficio) throws F3BException {

		UfficioController lUff = new UfficioController();
		return lUff.getDescTipoUffByCodUfficio(aCodTipoUfficio);
	}

	public static String getCodUfficioByCodTipoUfficioDescrComune(String aCodTipoUfficio, String aDescrComune)
			throws F3BException {

		UfficioController lUff = new UfficioController();
		UfficioModel lUffMod = lUff.getUfficioByCodTipoUffDescrComune(aCodTipoUfficio.toUpperCase(),
				aDescrComune.toUpperCase());
		return lUffMod.getCodUfficio();
	}

}