package siap.sico.utente.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaUtenteAttivo</p>
* <p>Description: Classe Action per la load ricerca degli Utenti Attivi, per ufficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaUtenteAttivo extends ActionSiap implements ICostantiUtente
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCA_UTENTE_ATTIVO;  //restituisce la jsp di VIEW
  }
}




