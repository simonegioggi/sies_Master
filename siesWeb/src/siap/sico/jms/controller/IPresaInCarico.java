package siap.sico.jms.controller;
/*
 Luigi 28-06-2006
 Questa interfaccia duplica l'analoga in siap.sius.jms.controller
 Superato il test quello verrà sostituito.
*/

import java.sql.Connection;

import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import f3b.util.F3BException;


public interface IPresaInCarico
{
  public MessaggioModel ExPresaInCaricoOrdinanza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
  throws F3BException;
  
  public MessaggioModel ExPresaInCaricoOrdinanza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt, Connection aConn )
  throws Exception;
  
  public  MessaggioModel ExPresaInCaricoDecreto(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
  throws Exception;
  
  public  MessaggioModel ExPresaInCaricoDecreto(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt, Connection aConn )
  throws Exception;
  
  public  MessaggioModel ExPresaInCaricoProvvedimento(MessaggioModel aMessaggio,Connection aConn )
  throws Exception;
  
  public  MessaggioModel ExPresaInCaricoRichiestaRelazione(MessaggioModel aMessaggio, Connection aConn )
  throws Exception;
  
  public MessaggioModel ExPresaInCaricoRicorso(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
  throws F3BException;
  
  public MessaggioModel ExPresaInCaricoAttivita(MessaggioModel aMessaggio)
  throws F3BException;
  
  public MessaggioModel ExPresaInCaricoAttivita(MessaggioModel aMessaggio, Connection aConn )
  throws Exception;
  
  public MessaggioModel ExPresaInCaricoRichiestaSiepe(MessaggioModel aMessaggio)
  throws F3BException;

  public MessaggioModel ExPresaInCaricoRichiestaSiepe(MessaggioModel aMessaggio, Connection aConn ) 
  throws Exception;
  
  public MessaggioModel ExPresaInCaricoSentenza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
  throws F3BException;

  public MessaggioModel ExPresaInCaricoSentenza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt, Connection aConn )
  throws Exception;

/*
  public MessaggioModel ExPresaInCaricoDecreto(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
    throws F3BException;
*/
}
