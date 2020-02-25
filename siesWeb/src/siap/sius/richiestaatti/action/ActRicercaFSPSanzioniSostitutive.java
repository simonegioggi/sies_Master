package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ActRicercaFSPuntuale;

/**
* <p>Title: ActRicercaFSPSanzioniSostitutive</p>
* <p>Description: Classe Action per l'esecuzione della ricerca puntuale 
*                 del fascicolo SIUS per la Sanzione Sostitutiva</p>
* <p>Company: Eunics S.p.A.</p>
*/
public class ActRicercaFSPSanzioniSostitutive extends ActRicercaFSPuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    //  Passando il parametro noQuery non effettua nuovamente la ricerca
    if( isRequestParameterNullObj( "noQuery") )
      super.processRequest();

    // Bottone di ritorno
    setLinkRitorno();

    // Ritorna la JSP di view dell'elenco stampe.
    return PG_ELENCOSTAMPEDOCISTRUTTORI;
  }
}