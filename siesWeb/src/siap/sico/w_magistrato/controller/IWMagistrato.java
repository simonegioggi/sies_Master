package siap.sico.w_magistrato.controller;

import java.util.Vector;

import siap.sico.w_magistrato.model.WMagistratoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: WMagistratoController
 * </p>
 * <p>
 * Description: Classe Controller per WMagistrato
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
public interface IWMagistrato {

	public WMagistratoModel ExInserisciWMagistrato(WMagistratoModel aWMagistrato) throws F3BException;

	public Vector ExRicercaWMagistrato(WMagistratoModel aWMagistrato) throws F3BException;

	public Vector ExRicercaWMagistratoByCognome(String aCognome) throws F3BException;

	public WMagistratoModel ExRicercaWMagistratoByKey(String aKey) throws F3BException;

	public WMagistratoModel ExModificaWMagistrato(WMagistratoModel aWMagistrato) throws F3BException;

	public void ExCancellaWMagistrato(WMagistratoModel aWMagistrato) throws F3BException;

}