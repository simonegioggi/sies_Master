package siap.siep.parametro.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.parametro.model.ParametroModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ParametroController
 * </p>
 * <p>
 * Description: Classe Controller per Parametro
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
public interface IParametro {

	public ParametroModel ExInserisciParametro(ParametroModel aParametro) throws F3BException;

	public Vector ExRicercaParametro(ParametroModel aParametro) throws F3BException;

	public ParametroModel ExRicercaParametroByKey(BigDecimal aKey) throws F3BException;

	public ParametroModel ExModificaParametro(ParametroModel aParametro) throws F3BException;

	public void ExCancellaParametro(ParametroModel aParametro) throws F3BException;

	public Vector ExRicercaParametroScadenzario(ParametroModel aParametro) throws F3BException;

	public Vector ExRicercaParametroUfficioConnesso(ParametroModel aParametro) throws F3BException;

	public ParametroModel ExRicercaParametroUfficioConnesso(String aNomeParametro, String aUfficioValidita)
			throws F3BException;

}