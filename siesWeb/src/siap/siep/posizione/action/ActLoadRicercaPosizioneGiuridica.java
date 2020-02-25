package siap.siep.posizione.action;


/**
* <p>Title: ActLoadRicercaPosizioneGiuridica</p>
* <p>Description: Classe Action per la load ricerca di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;

public class ActLoadRicercaPosizioneGiuridica extends ActionSiap implements ICostantiPosizioneGiuridica
{
  public String processRequest() throws Exception
  {
    return PG_LOAD_RICERCAPOSIZIONEGIURIDICA;  //restituisce la jsp di VIEW
  }
}