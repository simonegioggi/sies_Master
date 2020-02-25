package siap.siep.altracausa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import siap.siep.altracausa.model.AltraCausaModel;
import f3b.util.F3BException;

public interface IAltraCausa
{
  public AltraCausaModel ExRicercaAltraCausaByFascicolo(BigDecimal aKeyFascicolo) throws F3BException;

  public AltraCausaModel ExRicercaAltraCausaIstitutoByFascicolo(BigDecimal aKeyFascicolo) throws F3BException;

  public AltraCausaModel ExRicercaAltraCausaIstitutoByKey(BigDecimal aKeyAltraCausa)
  throws F3BException;
  
 //MEV_67
 public String ExInserisciAltraCausaWithoutSequence(AltraCausaModel altraCausa, Connection lConn) throws F3BException;
}