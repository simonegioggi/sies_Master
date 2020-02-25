package siap.sius.tenore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sius.tenore.model.TenoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TenoreController
 * </p>
 * <p>
 * Description: Classe Controller per Tenore
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
public interface ITenore {

	public TenoreModel ExInserisciTenore(TenoreModel aTenore) throws F3BException;

	public Vector ExRicercaTenore(TenoreModel aTenore) throws F3BException;

	public TenoreModel ExRicercaTenoreByKey(BigDecimal aKey) throws F3BException;

	public TenoreModel ExModificaTenore(TenoreModel aTenore) throws F3BException;

	public void ExCancellaTenore(TenoreModel aTenore) throws F3BException;

	public Vector ExRicercaTenoreByGenProc(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByGenProcOrderByPeso(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByOrdinanzaOrderByPeso(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByDecretoOrderByPeso(BigDecimal aKey, Connection aConn) throws F3BException;

	public Vector ExRicercaTenoreByDecreto(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByDecretoIrreperibilità(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByOrdinanza(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByDecretoOrderByPesoNoGenProc(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreByOrdinanzaOrderByPesoNoGenProc(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaTenoreBySentenzaOrderByPeso(BigDecimal aKey) throws F3BException;

}