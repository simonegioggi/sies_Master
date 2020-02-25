package siap.sius.esecuzionemisurasicurezza.action;

/**
 * <p>Title: Ricerca Esecuzione Misure Sicurezza</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;

public class ActLoadRicercaEsecuzioneMS extends ActionSiap implements ICostantiEsecuzioneMS
{
  public String processRequest() throws Exception
  {
    return PG_LOAD_RICERCA_ESECUZIONE_MS;  //restituisce la jsp di VIEW
  }
}
