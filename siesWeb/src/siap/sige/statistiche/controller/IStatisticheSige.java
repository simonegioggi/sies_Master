package siap.sige.statistiche.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IStatisticheSige
 * </p>
 * <p>
 * Description: interface per il controller di Statistiche SIGE.
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IStatisticheSige {

	public Vector ExRicercaFogliComplementariPaginata(RicercaFogliCompModel aModel, int aPageNum)
			throws F3BException;

	public BigDecimal ExGetNumRicercaFogliComplementari(RicercaFogliCompModel aModel) throws F3BException;

	public BigDecimal ExCountEstraiStatisticheFogliComplementari(RicercaFogliCompModel aModel)
			throws F3BException;

	public Vector<StatisticheFogliComplementariModel> ExEstraiStatisticheFogliComplementari(
			RicercaFogliCompModel aModel, int aPageNum) throws F3BException;

	public StatisticheFogliComplementariContainerModel ExEstraiStatisticheFogliComplementariExportExcel(
			RicercaFogliCompModel aModel) throws F3BException;

}