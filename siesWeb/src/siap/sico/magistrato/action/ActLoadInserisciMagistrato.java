package siap.sico.magistrato.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;


/**
* <p>Title: ActLoadInserisciMagistrato</p>
* <p>Description: Classe Action per la load inserisci di Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciMagistrato extends ActionSiap implements ICostantiMagistrato
{
  public String processRequest() throws F3BException
  {

    // Inserire Eventuali ComboBOX
    Option lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute( "elencoFlagStato", "" + lOption );
    setRequestAttribute("elencoFlagStato", "" + lOption );
    // Imposta Modalità.
    setRequestAttribute("modalita", "I");
    return PG_LOAD_INSERISCIMAGISTRATO;  //restituisce la jsp di VIEW

  }

}