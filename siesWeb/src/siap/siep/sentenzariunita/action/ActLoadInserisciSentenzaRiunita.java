package siap.siep.sentenzariunita.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSentenzaRiunita</p>
 * <p>Description: Classe Action per la load inserisci di SentenzaRiunita</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActLoadInserisciSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita
{
  public String processRequest() throws F3BException
  {
    // Inserire Eventuali ComboBOX
    // 23/06/2010 Sostituzione Elenco Autorità Emittenti
 		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
  	Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");

    // Imposta i provvedimenti Rif.
    setRequestAttribute("autoritaEmi", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "I");

    return PG_LOAD_INSERISCISENTENZARIUNITA;  //restituisce la jsp di VIEW
  }
}