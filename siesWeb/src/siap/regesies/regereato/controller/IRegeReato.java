package siap.regesies.regereato.controller;

import java.util.Vector;

import siap.regesies.regereato.model.RegeReatoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeReatoController
 * </p>
 * <p>
 * Description: Classe Interfaccia Controller per RegeReato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IRegeReato {

	// public RegeReatoModel ExInserisciRegeReato (RegeReatoModel aRegeReato )
	// throws F3BException;
	public Vector ExRicercaRegeReato(String aKey) throws F3BException;

	public RegeReatoModel ExRicercaRegeReatoByKey(String aKey, int aProgr, int aProgrCirc)
			throws F3BException;

	public RegeReatoModel ExModificaRegeReato(RegeReatoModel aRegeReato) throws F3BException;

	public void ExCancellaRegeReato(RegeReatoModel aRegeReato) throws F3BException;

	public Vector ExRicercaReatoCircostanzaByProvvedimento(String aKey) throws F3BException;

}