package siap.sius.presaincarico.action;

import siap.jms.ICostantiJMS;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadRicercaOrdinanzeRicevute</p>
* <p>Description: Classe Action per la load ricerca Ordinanze Ricevute</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaOrdinanzeRicevute extends ActionSiap implements ICostantiPresaincarico
{
  public String processRequest() throws Exception
  {
    //this.setLinkRitorno();
    // Imposta Tipo Ufficio SIUS.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSIUS());
    setRequestAttribute("tipoUfficioSIUS", "" + lOption );

    setRequestAttribute("function_name", "Ricerca Ordinanze Ricevute");
    setRequestAttribute("action_name", "siap.sius.presaincarico.action.ActRicercaProvvedimentiRicevuti");
    setRequestAttribute("tipo_provvedimento", ICostantiJMS.TRASFERIMENTO_ORDINANZA);
    return PG_LOAD_RICERCA_PROVV_SIUS;
  }
}