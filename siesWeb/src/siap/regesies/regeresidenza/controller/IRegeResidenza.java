package siap.regesies.regeresidenza.controller;

import java.util.Vector;

import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeResidenzaController
 * </p>
 * <p>
 * Description: Classe Controller per RegeResidenza
 * </p>
 */
@SuppressWarnings("rawtypes")
public interface IRegeResidenza {

	public Vector ExRicercaRegeResidenza(String aKey) throws F3BException;

	public RegeResidenzaModel ExRicercaRegeResidenzaByKey(String aKey, String aTipo) throws F3BException;

	public RegeResidenzaModel ExModificaRegeResidenza(RegeResidenzaModel aRegeResidenza) throws F3BException;

	public void ExCancellaRegeResidenza(String aKey, String aTipo) throws F3BException;

}