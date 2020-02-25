package siap.sius.presaincarico.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaAttiSiepe</p>
* <p>Description: Classe Action per la load ricerca di Atti Siepe </p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaAttiSiepe extends ActionSiap implements ICostantiPresaincarico
{
  public String processRequest() throws Exception
  {
    this.setLinkRitorno();
    // Imposta Tipo Procura.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
    lOption.setFilter( new String[] {"UEPE", "UEPESS"} );
    setRequestAttribute("tipoUfficioSIEPE", ("" + lOption).toUpperCase() );

    return PG_LOAD_RICERCAATTISIEPE;  //restituisce la jsp di VIEW
  }
}
