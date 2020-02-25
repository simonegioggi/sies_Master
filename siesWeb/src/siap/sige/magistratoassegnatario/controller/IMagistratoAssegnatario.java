package siap.sige.magistratoassegnatario.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoAssegnatarioController
 * </p>
 * <p>
 * Description: Classe Controller per MagistratoAssegnatario
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
public interface IMagistratoAssegnatario {

	public MagistratoAssegnatarioModel ExInserisciMagistratoAssegnatario(
			MagistratoAssegnatarioModel aMagistratoAssegnatario) throws F3BException;

	public MagistratoAssegnatarioModel ExRicercaMagAssCorrenteXFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public MagistratoAssegnatarioModel ExRicercaEstesaMagAssCorrenteXFascicolo(BigDecimal aKey)
			throws F3BException;

	public MagistratoAssegnatarioModel ExInserisciAggiornaMagistratoAssegnatario(
			MagistratoAssegnatarioMagistratoModel aMagistratoAssegnatarioMagistrato) throws F3BException;

	public Vector ExRicercaMagistratoByProcedimentoSige(BigDecimal aKeyFascicolo,
			String codUfficioUtenteConnesso) throws F3BException;

	// intervento per nuova gestione udienze monocratiche/collegiali per vers. 11.2.1
	public MagistratoAssegnatarioModel ExAggiornaMagistratoAssegnatarioXFascicolo (MagistratoAssegnatarioMagistratoModel aMagistratoAssegnatarioMagistrato, String flagBlocco)
			 throws F3BException;

}