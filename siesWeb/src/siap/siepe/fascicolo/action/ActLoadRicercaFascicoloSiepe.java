package siap.siepe.fascicolo.action;

/**
* <p>Title: ActLoadRicercaFascicoloSiepe</p>
* <p>Description: Classe Action per la load di RicercaFascicoloSiepe</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe
{
  public String processRequest() throws Exception
  {
   // Imposta Tipo Ufficio con Trattino.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSiepe());
    setRequestAttribute("tipoUfficioSiepeTrattino", "-" + lOption );

    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio );
    String strDescrComune = getUfficioUtenteConnesso().getDescrComune();
    setRequestAttribute("ComuneUfficioConnesso", strDescrComune );

    return PG_LOAD_RICERCAFASCICOLOSIEPE; //restituisce la jsp di VIEW
  }
}
