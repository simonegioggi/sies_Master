package siap.regesies.regecircostanza.controller;

import java.util.Vector;

import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeCircostanzaController
 * </p>
 * <p>
 * Description: Classe Controller per RegeCircostanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings("rawtypes")
public interface IRegeCircostanza {

	// public RegeCircostanzaModel ExInserisciRegeCircostanza (RegeCircostanzaModel aRegeCircostanza )
	// throws F3BException;

	public Vector ExRicercaRegeCircostanza(String aKey) throws F3BException;

	public RegeCircostanzaModel ExRicercaRegeCircostanzaByKey(String aKey, int aProgr) throws F3BException;

	public RegeCircostanzaModel ExModificaRegeCircostanza(RegeCircostanzaModel aRegeCircostanza)
			throws F3BException;

	public void ExCancellaRegeCircostanza(RegeCircostanzaModel aRegeCircostanza) throws F3BException;

}