package siap.siep.riepilogoprovvedimento.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RiepilogoProvvedimentoController
 * </p>
 * <p>
 * Description: Classe Controller per RiepilogoProvvedimento
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
public interface IRiepilogoProvvedimento {

	public RiepilogoProvvedimentoModel ExInserisciRiepilogoProvvedimento(
			RiepilogoProvvedimentoModel aRiepilogoProvvedimento) throws F3BException;

	public Vector ExRicercaRiepilogoProvvedimento(RiepilogoProvvedimentoModel aRiepilogoProvvedimento)
			throws F3BException;

	public RiepilogoProvvedimentoModel ExRicercaRiepilogoProvvedimentoByKey(BigDecimal aKey)
			throws F3BException;

	public RiepilogoProvvedimentoModel ExModificaRiepilogoProvvedimento(
			RiepilogoProvvedimentoModel aRiepilogoProvvedimento) throws F3BException;

	public void ExCancellaRiepilogoProvvedimento(RiepilogoProvvedimentoModel aRiepilogoProvvedimento)
			throws F3BException;

}