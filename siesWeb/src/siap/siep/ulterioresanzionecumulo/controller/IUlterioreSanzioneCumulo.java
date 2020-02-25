package siap.siep.ulterioresanzionecumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UlterioreSanzioneCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per UlterioreSanzioneCumulo
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
public interface IUlterioreSanzioneCumulo {

	public UlterioreSanzioneCumuloModel ExInserisciUlterioreSanzioneCumulo(
			UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo) throws F3BException;

	public Vector ExRicercaUlterioreSanzioneCumulo(UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo)
			throws F3BException;

	public UlterioreSanzioneCumuloModel ExRicercaUlterioreSanzioneCumuloByKey(BigDecimal aKey)
			throws F3BException;

	public UlterioreSanzioneCumuloModel ExModificaUlterioreSanzioneCumulo(
			UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo) throws F3BException;

	public void ExCancellaUlterioreSanzioneCumulo(UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo)
			throws F3BException;

	public Vector ExRicercaUlterioreSanzioneCumuloByFascicoloCumulante(FascicoloSiepModel aModel)
			throws F3BException;

	public UlterioreSanzioneCumuloModel ExInserisciOModificaUlterioriSanzioniCumulo(Vector aSanzioni,
			BigDecimal aIdFas) throws F3BException;

	/**
	 * Ricerca tutti i record Ulteriore_sanzione_cumulo collegati al fascicolo specificato e al CUMULO
	 * specificato.
	 *
	 * @param aIdFasc
	 *            - id del fascicolo cumulante
	 * @param aIdCum
	 *            - id del CUMULO
	 * @return Vector di UlterioreSanzioneCumuloModel
	 * @throws F3BException
	 */
	public Vector ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(BigDecimal aIdFasc, BigDecimal aIdCum)
			throws F3BException;

	public String ExInserisciUlterioreSanzioneCumuloWithoutSequence(ArrayList aUltSanCumuli, Connection lConn)
			throws F3BException;

}