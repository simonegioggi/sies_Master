package siap.siep.modulocumulo.controller;


/**
* <p>Title: ComputiCumuloController</p>
* <p>Description: Classe Controller per ComputiCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.ComputiCumuloModel;


public interface IComputiCumulo{

  public ComputiCumuloModel ExInserisciComputiCumulo (ComputiCumuloModel aComputiCumulo )  throws F3BException ;

  public Vector <ComputiCumuloModel> ExRicercaComputiCumulo (ComputiCumuloModel aComputiCumulo ) throws F3BException; 

  public void  ExModificaComputiCumulo (ComputiCumuloModel aComputiCumulo ) throws F3BException;

  public void ExCancellaComputiCumuloBykey (BigDecimal aIdComputiCumulo ) throws F3BException ;

  public BigDecimal ExGetCountComputiCumulo (ComputiCumuloModel aComputiCumulo ) throws F3BException ;

  public ComputiCumuloModel ExRicercaComputiCumuloById ( BigDecimal aIdComputiCumulo ) throws F3BException ;

  public Vector <ComputiCumuloModel> ExRicercaComputiCumuloPaged (ComputiCumuloModel aComputiCumulo,int aPage ) throws F3BException ; 

  public Vector <ComputiCumuloModel> ExRicercaComputiCumuloByIdIstruttoria (BigDecimal aIdIstruttoria, BigDecimal aIdDatiFinali)  throws F3BException; 
  
  // MEV 26 Cumulo Step2
  public String ExInserisciComputiCumuloWithoutSequence(Vector<ComputiCumuloModel> VecComputiCum , Connection lConn) throws F3BException;
  
  // MEV 70
  public Vector <ComputiCumuloModel> ExRicercaComputiCumuloByIdTitoloCum (BigDecimal aIdTitolo ) throws F3BException;
  
}
