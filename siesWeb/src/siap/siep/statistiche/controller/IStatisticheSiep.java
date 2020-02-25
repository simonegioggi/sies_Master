package siap.siep.statistiche.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.statistiche.model.RicercaFogliCompModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariModel;
import f3b.util.F3BException;

/**
* <p>Title: IStatisticheSige</p>
* <p>Description: interface per il controller di Statistiche SIGE.</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public interface IStatisticheSiep {
   
    
    public BigDecimal ExCountEstraiStatisticheFogliComplementari (RicercaFogliCompModel aModel) throws F3BException;
    public Vector <StatisticheFogliComplementariModel>ExEstraiStatisticheFogliComplementari (RicercaFogliCompModel aModel, int aPageNum ) throws F3BException;
    public StatisticheFogliComplementariContainerModel ExEstraiStatisticheFogliComplementariExportExcel (RicercaFogliCompModel aModel) throws F3BException;
    
}
