package siap.siep.annotazionemanuale.action;

import f3b.util.F3BException;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;


/**
 * <p>Title: ActLoadInserisciAnnotazioneManuale</p>
 * <p>Description: Classe Action per la load inserisci di AnnotazioneManuale</p>
 * 
 * <b>Attualmente (4/4/2006) questa classe non viene utilizzata (generata dal CG)</b>
 * 
 */

public class ActLoadInserisciAnnotazioneManuale extends ActionSiap implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws F3BException {
    setRequestAttribute("modalita", "I");
    return PG_LOAD_INSERISCIANNOTAZIONEMANUALE;  //restituisce la jsp di VIEW 
  }
}