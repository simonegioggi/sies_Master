package siap.sige.datiprovsige.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;

/**
 * <p>
 * Title: DatiProvvedimentoSigeController
 * </p>
 * <p>
 * Description: Classe Controller per DatiProvvedimentoSige
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
public interface IDatiProvvedimentoSige {

	public Vector ExRicercaDatiProvvedimentoSigeByIdTenore(BigDecimal aIdTenore) throws F3BException;

}