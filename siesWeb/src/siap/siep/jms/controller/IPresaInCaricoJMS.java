package siap.siep.jms.controller;

import siap.jms.ICostantiJMS;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.model.PresaInCaricoModel;
import f3b.util.F3BException;

public interface IPresaInCaricoJMS extends ICostantiJMS
{
  /*
  public MessaggioModel ExInserisciIstanzaTrasmessa(MessaggioModel aMessaggio)
  throws F3BException;
  
  public MessaggioModel ExInserisciNuovaIstanzaTrasmessa(MessaggioModel aMessaggio)
  throws F3BException;
  
  public MessaggioModel ExInserisciProvvedimentoTrasmesso(MessaggioModel aMessaggio)
  throws F3BException;
  
  public MessaggioModel ExInserisciFascicoloSiep(MessaggioModel aMessaggio)
  throws F3BException;
  */
  
  // MEV_2024-DNA - Modificati i metodi, le Action devono passare le informazioni sull'utente che sta 
  //                acquisendo il fascicolo per tracciarlo in banca dati
  public MessaggioModel ExInserisciIstanzaTrasmessa(MessaggioModel aMessaggio, PresaInCaricoModel aPresaInCaricoModel)
  throws F3BException;

  public MessaggioModel ExInserisciNuovaIstanzaTrasmessa(MessaggioModel aMessaggio, PresaInCaricoModel aPresaInCaricoModel)
  throws F3BException;

  public MessaggioModel ExInserisciProvvedimentoTrasmesso(MessaggioModel aMessaggio, PresaInCaricoModel aPresaInCaricoModel)
  throws F3BException;
  
  public MessaggioModel ExInserisciFascicoloSiep(MessaggioModel aMessaggio, PresaInCaricoModel aPresaInCaricoModel)
  throws F3BException;
  //MEV_2024-DNA - FINE
}