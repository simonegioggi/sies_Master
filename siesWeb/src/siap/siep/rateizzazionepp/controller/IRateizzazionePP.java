package siap.siep.rateizzazionepp.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;

public interface IRateizzazionePP {
    public void exInserisciRateizzazioni (Vector <RateizzazionePPModel> aListaRate) throws F3BException;
    
    public Vector <RateizzazionePPModel> exRicercaRateizzazioniByIdFasc (BigDecimal aIdFasc) throws F3BException;
    
    public void exCancellaRateizzazioniByIdFasc (BigDecimal aIdFasc) throws F3BException;
    
    public void exModificaRateizzazioni (Vector <RateizzazionePPModel> aListaRate, BigDecimal aIdFasc) throws F3BException;
}
