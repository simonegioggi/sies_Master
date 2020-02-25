package siap.sius.produzioneatti.action;

/**
* <p>Title: ActLoadRicercaFSProvvedimenti</p>
* <p>Description: Classe Action per la load di RicercaFSProvvedimenti,
* cioè la ricerca prima del Fascicolo Sius e quindi
* dei provvedimenti collegati.</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

public class ActLoadFSPEsitoParereInamm extends ActLoadRicercaFSPuntuale
implements ICostantiFascicoloSius, ICostantiProduzioneAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Esito parere ");
    setRequestAttribute("nextAction", "siap.sius.produzioneatti.action.ActRicercaFSEsitoParereInamm");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}
