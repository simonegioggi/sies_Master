package siap.siepe.assistentesociale.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaAssistenteSocialeLista</p>
* <p>Description: Classe Action per la load ricerca di AssistenteSociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadRicercaAssistenteSocialeLista extends ActionSiap
implements ICostantiAssistenteSociale
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCA_ASSISTENTESOCIALE_LISTA;  //restituisce la jsp di VIEW
  }
}

