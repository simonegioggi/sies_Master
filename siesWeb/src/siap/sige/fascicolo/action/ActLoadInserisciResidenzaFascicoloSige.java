package siap.sige.fascicolo.action;

/**
 * <p>Title: ActLoadInserisciResidenzaFascicoloSige</p>
 * <p>Description: Classe Action per la load inserisci della residenza fascicolo SIGE</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @version 1.0
 */

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadInserisciResidenzaFascicoloSige extends ActionSiap implements ICostantiFascicoloSige
{
  public String processRequest() throws Exception
  {
    // punto di Ritorno
    gestioneRitorno();

    setRequestAttribute("modalita", "I");

    //Per default ITALIA (039)
    String lCodStato = "039";
      Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), lCodStato);
    setRequestAttribute("nazioni", "" + lOption );

    return PG_LOAD_INSERISCIRESIDENZAFASCICOLOSIGE;  //restituisce la jsp di VIEW
  }
}