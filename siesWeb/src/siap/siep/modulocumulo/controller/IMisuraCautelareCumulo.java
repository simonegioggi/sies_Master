package siap.siep.modulocumulo.controller;


/**
* <p>Title: MisuraCautelareCumuloController</p>
* <p>Description: Classe Controller per MisuraCautelareCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;


public interface IMisuraCautelareCumulo{

  public MisuraCautelareCumuloModel ExInserisciMisuraCautelareCumulo (MisuraCautelareCumuloModel aMisuraCautelareCumulo )  throws F3BException ;

  public Vector <MisuraCautelareCumuloModel> ExRicercaMisuraCautelareCumulo (MisuraCautelareCumuloModel aMisuraCautelareCumulo ) throws F3BException; 

  public Vector <MisuraCautelareCumuloModel> ExRicercaMisureCautelariCumuloByIdTitolo (BigDecimal aIdTitolo) throws F3BException; 

  public void  ExModificaMisuraCautelareCumulo (MisuraCautelareCumuloModel aMisuraCautelareCumulo ) throws F3BException;

  public void ExCancellaMisuraCautelareCumulo (BigDecimal aIdMisuraCautelareCumulo) throws F3BException ;

  public void  ExCancellaMisuraCautelareCumuloLogica (MisuraCautelareCumuloModel aMisuraCautelareCumulo ) throws F3BException;

  public BigDecimal ExGetCountMisuraCautelareCumulo (MisuraCautelareCumuloModel aMisuraCautelareCumulo ) throws F3BException ;

  public MisuraCautelareCumuloModel ExRicercaMisuraCautelareCumuloById ( BigDecimal aIdMisuraCautelareCumulo ) throws F3BException ;

  public Vector <MisuraCautelareCumuloModel> ExRicercaMisuraCautelareCumuloPaged (MisuraCautelareCumuloModel aMisuraCautelareCumulo,int aPage ) throws F3BException ;
  
  // MEV 26 CUMULO Step2
  public String ExInserisciMisuraCautelareCumuloWithoutSequence(Vector<MisuraCautelareCumuloModel> aMisureCautelariCumulo, Connection lConn )  throws F3BException;

}
