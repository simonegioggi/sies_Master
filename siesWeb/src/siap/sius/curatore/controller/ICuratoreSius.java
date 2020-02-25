package siap.sius.curatore.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.curatore.model.CuratoreSiusModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CuratoreSiusController
 * </p>
 * <p>
 * Description: Classe Controller per Curatore Sius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ICuratoreSius {

	public CuratoreSiusModel ExInserisciCuratoreSius(CuratoreSiusModel aCurSiusMod) throws F3BException;

	public CuratoreSiusModel ExRicercaCurSiusByFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaCuratoreCorrentePrecedenteByFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaCurSiusByFascicolo(BigDecimal aKey, String codUfficioUtenteConnesso)
			throws F3BException;

	public void ExCancellaCuratoreSius(BigDecimal IdCuratore, BigDecimal IdFascicolo) throws F3BException;
	// public CuratoreSiusModel ExRicercaEstesaCurSiusByFascicolo( BigDecimal aKey )
	// throws F3BException;

}