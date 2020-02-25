package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
* <p>Title: ActRicercaFSPSanzioniSostitutive</p>
* <p>Description: Classe Action per la chiamata della maschera per la 
*                 puntuale del fascicolo SIUS per la Sanzione Sostitutiva</p>
* <p>Company: Eunics S.p.A.</p>
*/
public class ActLoadFSPSanzioniSostitutive extends ActLoadRicercaFSPuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Sanzioni Sostitutive");
    setRequestAttribute("nextAction", "siap.sius.richiestaatti.action.ActRicercaFSPSanzioniSostitutive");
    // Si invoca il metodo della superclasse, e ritorna la pagina da visualizzare
    return super.processRequest();
  }
}