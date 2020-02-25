package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import f3b.util.F3BException;

/**
* <p>Title: ContinuazioneCumuloController</p>
* <p>Description: Classe Controller per Continuazione</p>
*  <p>		in ambito Cumulo (Continuazione_Cumulo) </p>
* @version 1.0
*/

public interface IContinuazioneCumulo
{
  public ContinuazioneCumuloModel ExInserisciContinuazione (ContinuazioneCumuloModel aContinuazione )
    throws F3BException;

  public Vector <ContinuazioneCumuloModel> ExRicercaContinuazione (ContinuazioneCumuloModel aContinuazione )
    throws F3BException;

  public Vector <ContinuazioneCumuloModel> ExRicercaContinuazioneByIDPenaComplessiva (BigDecimal aKey)
  throws F3BException;
  
  public ContinuazioneCumuloModel ExRicercaContinuazioneCumByKey (BigDecimal aKey)
    throws F3BException;

  public ContinuazioneCumuloModel ExModificaContinuazioneCum (ContinuazioneCumuloModel aContinuazione )
    throws F3BException;

  public void ExCancellaContinuazioneCum (ContinuazioneCumuloModel aContinuazione )
    throws F3BException;

  public Vector <ContinuazioneCumuloModel> ExRicercaContinuazioneByIDTitolo ( BigDecimal aIdTitolo) throws F3BException;


}
