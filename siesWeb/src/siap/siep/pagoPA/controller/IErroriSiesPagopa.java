package siap.siep.pagoPA.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.pagoPA.model.ErroriSiesPagopaModel;

public interface IErroriSiesPagopa {

  public ErroriSiesPagopaModel ExInserisciErroreSiesPagopa (ErroriSiesPagopaModel errModel) throws F3BException;
  
  public ErroriSiesPagopaModel ExRicercaErroreSiesPagopaByKey(BigDecimal idErrore)
      throws F3BException;
  
  public void ExCancellaErroreSiesPagopa(BigDecimal idErrore) throws F3BException;
  
  public Vector <ErroriSiesPagopaModel> ExRicercaErroriSiesPagopaByCriteria (ErroriSiesPagopaModel aCriteriRicerca, int aPage)
      throws F3BException;
  
  public ErroriSiesPagopaModel ExInserisciAggiornaErroreSiesPagopa (ErroriSiesPagopaModel errModel) throws F3BException;
  
  public void ExRimuoviErroreSiesPagopa (ErroriSiesPagopaModel errModel) throws F3BException;
}
