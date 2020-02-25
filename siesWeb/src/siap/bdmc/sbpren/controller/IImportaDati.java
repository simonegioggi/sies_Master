package siap.bdmc.sbpren.controller;

import java.math.BigDecimal;

import siap.bdmc.sbpren.model.EsitoImportModel;
import f3b.util.F3BException;


/**
* <p>Title: IImportaDati</p>
* <p>Description: Classe Interfaccia per importaDati</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/

public interface IImportaDati
{



public BigDecimal ExImportaProvvedimentoBDMC(EsitoImportModel aEsitoProvvedimento)
             throws F3BException;
public EsitoImportModel getEsito();


public BigDecimal ExIntegraFascicoloSiep(EsitoImportModel aEsitoProvvedimento)
             throws F3BException;

}
