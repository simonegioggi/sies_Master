package siap.sius.rifasius.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RiferimentoFascicoloSiusController
 * </p>
 * <p>
 * Description: Classe Controller per RiferimentoFascicoloSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IRiferimentoFascicoloSius {

	public RiferimentoFascicoloSiusModel ExInserisciRiferimentoFascicoloSius(
			RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius) throws F3BException;

	public Vector ExRicercaRiferimentoFascicoloSius(RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius)
			throws F3BException;

	public RiferimentoFascicoloSiusModel ExRicercaRiferimentoFascicoloSiusByKey(BigDecimal aKey)
			throws F3BException;

	public RiferimentoFascicoloSiusModel ExModificaRiferimentoFascicoloSius(
			RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius) throws F3BException;

	public void ExCancellaRiferimentoFascicoloSius(RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius)
			throws F3BException;

}