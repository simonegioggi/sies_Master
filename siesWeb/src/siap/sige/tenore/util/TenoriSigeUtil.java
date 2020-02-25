package siap.sige.tenore.util;

import java.util.Vector;

import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;

/**
 * <p>
 * Title: TenoriSigeUtil.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
public class TenoriSigeUtil {

	// Costruttore semplice
	public TenoriSigeUtil() {
	}

	public Vector<TenoreSigeModel> listaTenoriDaListaTenoriEstesi(
			Vector<TenoreSigeEstesoModel> aListaTenoriEstesi) {
		Vector<TenoreSigeModel> lTenori = new Vector<TenoreSigeModel>();
		if (aListaTenoriEstesi != null) {
			for (TenoreSigeEstesoModel lTenoreEstesoCorr : aListaTenoriEstesi) {
				lTenori.add(lTenoreEstesoCorr.getTenoreSige());
			}
		} // endif

		return lTenori;
	}

}