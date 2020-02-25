package siap.sius.esecuzionesanzionesostitutiva.action;

/**
 * <p>Title: Ricerca Esecuzione Sanzioni Sostitutive</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;

public class ActLoadRicercaEsecuzioneSS extends ActionSiap implements ICostantiEsecuzioneSS
{
  public String processRequest() throws Exception
  {
    return PG_LOAD_RICERCA_ESECUZIONE_SS;  //restituisce la jsp di VIEW
  }
}
