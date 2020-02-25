package siap.siep.reatopredisposto.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.reatopredisposto.model.ReatoPredispostoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ReatoPredispostoController
 * </p>
 * <p>
 * Description: Classe Controller per ReatoPredisposto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IReatoPredisposto {

	public ReatoPredispostoModel ExInserisciReatoPredisposto(ReatoPredispostoModel aReatoPredisposto)
			throws F3BException;

	public Vector ExRicercaReatoPredisposto(ReatoPredispostoModel aReatoPredisposto) throws F3BException;

	public ReatoPredispostoModel ExRicercaReatoPredispostoByKey(BigDecimal aKey) throws F3BException;

	public ReatoPredispostoModel ExModificaReatoPredisposto(ReatoPredispostoModel aReatoPredisposto)
			throws F3BException;

	public void ExCancellaReatoPredisposto(ReatoPredispostoModel aReatoPredisposto) throws F3BException;

}