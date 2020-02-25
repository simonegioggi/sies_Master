package siap.siepe.assistentesociale.action;

//import per le combo
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciAssistnenteSociale</p>
* <p>Description: Classe Action per la load inserisci di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciAssistenteSociale extends ActionSiap
implements ICostantiAssistenteSociale
{
/**
  * Carica la form per l'inserimento del Esperto.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    // Inserire Eventuali ComboBOX
    Option lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute( "elencoFlagStato", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "I");
    return PG_LOAD_INSERISCIASSISTENTESOCIALE;  //restituisce la jsp di VIEW
  }
}
