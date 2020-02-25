package siap.siepe.assistentesociale.action;

import siap.sico.web.ActionSiap;

/**
* <p>Title: ActLoadRicercaAssistenteSociale</p>
* <p>Description: Classe Action per la load ricerca di AssistenteSociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadRicercaAssistenteSociale extends ActionSiap
implements ICostantiAssistenteSociale
{
/**
  * Carica la form per la ricerca di un AssistenteSociale.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception
  */
  public String processRequest() throws Exception
  {

    return PG_LOAD_RICERCAASSISTENTESOCIALE;  //restituisce la jsp di VIEW
  }
}
