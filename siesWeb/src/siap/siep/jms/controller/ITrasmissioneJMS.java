package siap.siep.jms.controller;

import java.math.BigDecimal;

import siap.jms.messaggio.model.MessaggioModel;
import f3b.util.F3BException;

/**
 * <p>Title:ITrasmissioneJMS </p>
 * <p>Description:Interfaccia per MessageController </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public interface ITrasmissioneJMS
{
  public MessaggioModel getMessageForNuovaIstanza(BigDecimal aKeyEvento, BigDecimal aKeyFascicolo)
  throws F3BException;
  public MessaggioModel getMessageForIstanza(BigDecimal aKeyEvento, BigDecimal aKeyFascicolo)
    throws F3BException;
  public MessaggioModel getMessageForProvvedimento(BigDecimal aKeyEvento, BigDecimal aKeyFascicolo)
    throws F3BException;
}