package siap.bdmc.fascicolosiepbdmc.controller;


/**
* <p>Title: FascicoloSiepBdmcController</p>
* <p>Description: Classe Controller per FascicoloSiepBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IFascicoloSiepBdmc{

  public FascicoloSiepBdmcModel ExInserisciFascicoloSiepBdmc (FascicoloSiepBdmcModel aFascicoloSiepBdmc )  throws F3BException ;

  public Vector ExRicercaFascicoloSiepBdmc (FascicoloSiepBdmcModel aFascicoloSiepBdmc ) throws F3BException; 

  public void  ExModificaFascicoloSiepBdmc (FascicoloSiepBdmcModel aFascicoloSiepBdmc ) throws F3BException;

  public void ExCancellaFascicoloSiepBdmc (FascicoloSiepBdmcModel aFascicoloSiepBdmc ) throws F3BException ;

  public BigDecimal ExGetCountFascicoloSiepBdmc (FascicoloSiepBdmcModel aFascicoloSiepBdmc ) throws F3BException ;

  public FascicoloSiepBdmcModel ExRicercaFascicoloSiepBdmcById ( BigDecimal aIdFascicoloBdmc ) throws F3BException ;

  public Vector ExRicercaFascicoloSiepBdmcPaged (FascicoloSiepBdmcModel aFascicoloSiepBdmc,int aPage ) throws F3BException ; 

}