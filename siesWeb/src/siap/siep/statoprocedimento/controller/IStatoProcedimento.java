package siap.siep.statoprocedimento.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: StatoProcedimentoController
 * </p>
 * <p>
 * Description: Classe Controller per StatoProcedimento
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
public interface IStatoProcedimento {

	public StatoProcedimentoModel ExInserisciStatoProcedimento(StatoProcedimentoModel aStatoProcedimento)
			throws F3BException;

	public Vector ExRicercaStatoProcedimento(StatoProcedimentoModel aStatoProcedimento) throws F3BException;

	public StatoProcedimentoModel ExRicercaStatoProcedimentoByKey(BigDecimal aKey) throws F3BException;

	public StatoProcedimentoModel ExModificaStatoProcedimento(StatoProcedimentoModel aStatoProcedimento)
			throws F3BException;

	public String ExGetMaxStatoProcedimentoByFascicoloSiep(BigDecimal aKeyFascicolo) throws F3BException;

	public void ExCancellaStatoProcedimento(StatoProcedimentoModel aStatoProcedimento) throws F3BException;

	public Vector ExRicercaStatoProcedimentoByFascicoloSiep(BigDecimal aKeyFascicolo) throws F3BException;

	public Vector ExRicercaStatoProcedimentoByFascicoloSiepStato(BigDecimal aKeyFascicolo)
			throws F3BException;

	public String ExInserisciStatoProcedimentoWithoutSequence(ArrayList aStatoProcedimento, Connection lConn)
			throws F3BException;

	public StatoProcedimentoModel ExCancellaInserisciStatoProcedimento(
			StatoProcedimentoModel aStatoProcedimento) throws F3BException;

}