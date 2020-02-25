package siap.regesies.regenotiziareato.controller;

import java.util.Vector;

import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeNotiziaReatoController
 * </p>
 * <p>
 * Description: Classe Controller per RegeNotiziaReato
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
public interface IRegeNotiziaReato {

	public Vector ExRicercaRegeNotiziaReato(String aKey) throws F3BException;

	public RegeNotiziaReatoModel ExRicercaRegeNotiziaReatoByKey(String aKey, int aProgr) throws F3BException;

	public RegeNotiziaReatoModel ExModificaRegeNotiziaReato(RegeNotiziaReatoModel aRegeNotiziaReato)
			throws F3BException;

	public void ExCancellaRegeNotiziaReato(RegeNotiziaReatoModel aRegeNotiziaReato) throws F3BException;

}