package siap.sius.cancassfascsius.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CancAssFascSiusController
 * </p>
 * <p>
 * Description: Classe Controller per CancAssFascSius
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
public interface ICancAssFascSius {

	public CancAssFascSiusModel ExInserisciCancAssFascSius(CancAssFascSiusModel aCancAssFascSius)
			throws F3BException;

	public Vector ExRicercaCancAssFascSius(CancAssFascSiusModel aCancAssFascSius) throws F3BException;

	public CancAssFascSiusModel ExRicercaCancAssFascSiusAttiva(BigDecimal aIdFascicolo) throws F3BException;

	public void ExCancellaCancAssFascSius(CancAssFascSiusModel aCancAssFascSius) throws F3BException;

}