package siap.sige.provvedimento.action;

/**
* <p>Title: ActLoadRicercaFSPNotifica</p>
* <p>Description: Classe Action per la load di RicercaFSPNotifica</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadRicercaFSigePNotifica extends ActLoadRicercaFSigePuntuale
{
  public String processRequest() throws Exception
  {
    //super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    setRequestAttribute("nextAction", "siap.sige.provvedimento.action.ActRicercaFSigePNotifica" );
    setRequestAttribute("functionName", "Ricerca Provvedimenti per Notifica" );
    return PG_LOAD_RICERCAFSIGEPUNTUALE; //restituisce la jsp di VIEW.
  }
}