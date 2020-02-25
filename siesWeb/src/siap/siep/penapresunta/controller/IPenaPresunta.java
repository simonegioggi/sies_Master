package siap.siep.penapresunta.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siep.penapresunta.model.PenaPresuntaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PenaPresuntaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaPresunta
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
public interface IPenaPresunta {

	public PenaPresuntaModel ExInserisciPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException;

	public Vector ExRicercaPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException;

	public PenaPresuntaModel ExRicercaPenaPresuntaByKey(BigDecimal aKey) throws F3BException;

	public PenaPresuntaModel ExModificaPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException;

	public void ExCancellaPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException;

	public void ExCancellaPenaPresuntaByIdFascicolo(PenaPresuntaModel aPenaPresunta) throws F3BException;

	public PenaPresuntaModel ExRicercaPenaPresuntaCorrenteByFascicoloSiep(BigDecimal aKey)
			throws F3BException;

	public String ExInserisciPenaPresuntaWithoutSequence(PenaPresuntaModel aPenaPresunta, Connection lConn)
			throws F3BException;

}