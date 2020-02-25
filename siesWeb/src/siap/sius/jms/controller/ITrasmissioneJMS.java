package siap.sius.jms.controller;

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
  public MessaggioModel getMessageForOrdinanza(BigDecimal aKeyEvento)
    throws F3BException;

  public MessaggioModel getMessageForDecreto(BigDecimal aKeyEvento)
    throws F3BException;

  public MessaggioModel getMessageForRiFaSius(BigDecimal annoFaSius, BigDecimal progrFaSius, String codUfficioFaSius, BigDecimal idFascicoloSiep )
    throws F3BException;

  public MessaggioModel getMessageForImpugnazione(BigDecimal lEveKey, String lTipoProvvedimento )
    throws F3BException;

  public MessaggioModel getMessageForRichiesta(BigDecimal aKeyEvento)
    throws F3BException;

  public MessaggioModel getMessageForSentenza(BigDecimal aKeyEvento)
		    throws F3BException;

  public MessaggioModel getMessageForOpposizioneRicorso(BigDecimal aKeyEvento, BigDecimal aKeyImpugSige)
		    throws F3BException;

}