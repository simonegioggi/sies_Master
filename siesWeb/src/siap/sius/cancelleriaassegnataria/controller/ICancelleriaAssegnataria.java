package siap.sius.cancelleriaassegnataria.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CancelleriaAssegnatariaController
 * </p>
 * <p>
 * Description: Classe Controller per CancelleriaAssegnataria
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
public interface ICancelleriaAssegnataria {

	public void ExCancellaCancelleriaAssegnataria(CancelleriaAssegnatariaModel aCancelleriaAssegnataria)
			throws F3BException;

	public BigDecimal ExGetNumRicercaCancelleriaAssegnatariaPagina(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) throws F3BException;

	public CancelleriaAssegnatariaModel ExInserisciCancelleriaAssegnataria(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) throws F3BException;

	public CancelleriaAssegnatariaModel ExModificaCancelleriaAssegnataria(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) throws F3BException;

	public Vector ExRicercaCancelleriaAssegnataria(CancelleriaAssegnatariaModel aCancelleriaAssegnataria)
			throws F3BException;

	public Vector ExRicercaCancelleriaAssegnatariaPagina(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria, int aPageNum) throws F3BException;

}