package siap.siep.misurasicurezza.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Action per la Load della popup di restituzione atti ricevuti per competenza
 * per iscrizione del fscicolo di esecuzione delle Misure di Sicurezza
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadRestituzioneAttoRicevuto extends ActionSiap implements ICostantiMisuraSicurezza
{
  public String processRequest() throws F3BException
  {   
    return  PG_LOAD_RESTITUZIONE_ATTO_RICEVUTO_MS;
  }
}
