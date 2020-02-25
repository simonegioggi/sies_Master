package siap.sius.scadenzario.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ScadenzarioSiusController
 * </p>
 * <p>
 * Description: Classe Controller per ScadenzarioSius
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
public interface IScadenzarioSius {

	public ScadenzarioSiusModel ExInserisciScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException;

	public Vector ExRicercaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius) throws F3BException;

	public ScadenzarioSiusModel ExRicercaScadenzarioSiusByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaScadenzarioSius(Date aData1, Date aData2) throws F3BException;

	public Vector ExRicercaScadenzarioSius(String aTipoScadenzario, Date aData1, Date aData2)
			throws F3BException;

	public ScadenzarioSiusModel ExModificaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException;

	public void ExCancellaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius) throws F3BException;

	public ScadenzarioSiusModel ExRicercaScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo,
			String aTipo) throws F3BException;

	public boolean ExScadutoScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws F3BException;

	public ScadenzarioSiusModel ExModificaScadenzario(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException;

	public void ExSetVistoScadenzario(ScadenzarioSiusModel aScadenzarioSius) throws F3BException;

	public Vector ExElencoTipiScadenzarioByTipoUfficio(String aTipoUfficio) throws F3BException;

}