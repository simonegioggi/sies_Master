package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ICostantiFascicoloSius;


/**
 * <p>Title: ActVisualizzaStatoAtti </p>
 * <p>Description: Azione specializzazione della  ActRicercaStatoAtti.
 * Usa il metodo <code>ricercaAtti()<code> del padre per ricavare la lista degli
 * atti istruttori richiesti per un fascicolo SIUS specifico.
 * A differenza della classe padre non fornisce il dettaglio del fascicolo SIUS ed
 * una lista modificabile ma attraverso la jsp comune fornisce solo la lista Atti
 * in modalità di solo lettura.
 * </p>
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */


public class ActVisualizzaStatoAtti extends ActRicercaStatoAtti
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    String lPage = new String();
    this.setLinkRitorno();
      lPage = ricercaAtti(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
      setRequestAttribute("modalita", "V");
    // Ritorna la JSP di view dell'elenco Atti.
      return lPage;
  }
}