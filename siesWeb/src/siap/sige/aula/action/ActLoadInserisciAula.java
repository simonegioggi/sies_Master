package siap.sige.aula.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciAula</p>
 * <p>Description: Classe Action per la load Inserimento Aula</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
*/
public class ActLoadInserisciAula extends ActionSiap implements ICostantiAula
{
/**
  * Carica la form per l'inserimento dell'Aula.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception propaga errore di eccezione.
  */
  public String processRequest() throws Exception
  {
 
    //Prepara la pagina di destinazione
    String lPage = PG_LOAD_INSERISCI_AULA;

    gestioneRitorno();
    
    Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    setRequestAttribute("elencoSezioni", "" + lOption );

    //Flag Aula Predefinita
    lOption = new Option( DecodificheManager.getInstance().getFlagSN(), "N");
    setRequestAttribute("aulaPredefinita", "" + lOption );    

    // Imposta Modalità Inserimento.
    setRequestAttribute("modalita", "I");

    return lPage;  //restituisce la jsp di VIEW
    
  }

}