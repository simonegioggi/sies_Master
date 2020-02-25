package siap.sius.unificazione.action;

/**
* <p>Title: ActLoadUnificazioneSoggetti</p>
* <p>Description: Classe Action per la Load di UnificazioneSoggetti</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadUnificazioneSoggetti extends ActionSiap
implements ICostantiUnificazione
{
  public String processRequest() throws Exception
  {
    // Imposta Tipo Ufficio SIUS.
    String lTipoUfficio = this.getUfficioUtenteConnesso().getCodTipoUfficio();
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSIUS(), lTipoUfficio);
    setRequestAttribute("tipoUfficioSIUS", "" + lOption );
    setRequestAttribute("TipoUfficioConnesso", getUfficioUtenteConnesso().getCodTipoUfficio() );

    return PG_LOAD_UNIFICAZIONESOGGETTI; //restituisce la jsp di VIEW
  }
}