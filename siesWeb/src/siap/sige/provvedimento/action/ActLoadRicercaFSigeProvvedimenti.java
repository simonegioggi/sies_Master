package siap.sige.provvedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

/**
* <p>Title: ActLoadRicercaFSigeProvvedimenti</p>
* <p>Description: Classe Action per la load di RicercaFSigeProvvedimenti,
* cioè la ricerca prima del Fascicolo Sige e quindi dei provvedimenti collegati.</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/

public class ActLoadRicercaFSigeProvvedimenti extends ActLoadRicercaFSigePuntuale
	implements ICostantiProvvedimentoSige
{
  public String processRequest() throws Exception
  {
    //super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    setRequestAttribute("nextAction", "siap.sige.provvedimento.action.ActRicercaFSigeProvvedimenti" );
    setRequestAttribute("functionName", "Ricerca Provvedimenti" );
    return PG_LOAD_RICERCAFSIGEPUNTUALE; //restituisce la jsp di ricerca Fascicolo Sige.
  }
}