package siap.sius.esecuzionemisuraalternativa.action;

/**
 * <p>Title: Ricerca Esecuzione Misure Alternative</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;

public class ActLoadRicercaEsecuzioneMA extends ActionSiap implements ICostantiEsecuzioneMA
{
  public String processRequest() throws Exception
  {
    return PG_LOAD_RICERCA_ESECUZIONE_MA;  //restituisce la jsp di VIEW
  }
}
