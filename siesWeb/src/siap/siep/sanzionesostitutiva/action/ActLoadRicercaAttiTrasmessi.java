package siap.siep.sanzionesostitutiva.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaAttiTrasmessi</p>
* <p>Description: Classe Action per la load della Lista Messaggi Inviati</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadRicercaAttiTrasmessi extends ActionSiap implements ICostantiSanzioneSostitutiva
{
  public String processRequest() throws Exception
  {
     this.setLinkRitorno();    // Imposta la combo dei Tipi di Operazioni.

    // Imposta Tipo Ufficio.
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSius());
    setRequestAttribute("tipoUfficio", "" + lOption );
    
    
    return PG_LOAD_RICERCA_TRASMISSIONE_ATTI_ESECUZIONE;  //restituisce la jsp di VIEW
  }
}