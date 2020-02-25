package siap.siepe.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaAttiVistiPerTipo</p>
* <p>Description: Classe Action per la load ricerca di Messaggi in arrivo al SIEPE</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaAttiVistiPerTipo extends ActionSiap implements ICostantiFascicoloSiepe
{
  public String processRequest() throws Exception
  {
    this.setLinkRitorno();
    // Imposta Tipo Atto.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAtto());
    setRequestAttribute("tipoAtto", "" + lOption );

    return PG_LOAD_RICERCAATTIVISTIPERTIPO;  //restituisce la jsp di VIEW

  }

}
