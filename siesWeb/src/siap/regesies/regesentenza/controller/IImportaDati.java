package siap.regesies.regesentenza.controller;

import java.math.BigDecimal;

import siap.regesies.regesentenza.model.EsitoImportModel;
import f3b.util.F3BException;


/**
* <p>Title: IImportaDati</p>
* <p>Description: Classe Interfaccia per importaDati</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/

public interface IImportaDati
{



public BigDecimal ExImportaProvvedimentoRege(EsitoImportModel aRegeProvvedimento)
             throws F3BException;
public EsitoImportModel getEsito();


public BigDecimal ExIntegraFascicoloSiep(EsitoImportModel aRegeProvvedimento)
             throws F3BException;

}
