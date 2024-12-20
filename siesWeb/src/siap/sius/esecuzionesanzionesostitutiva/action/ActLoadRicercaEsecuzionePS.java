package siap.sius.esecuzionesanzionesostitutiva.action;

/**
 * <p>Title: Ricerca Esecuzione Pene Sostitutive</p>
 * @since MEV_2023-35
 */
import siap.sico.web.ActionSiap;

public class ActLoadRicercaEsecuzionePS extends ActionSiap implements ICostantiEsecuzioneSS
{
  public String processRequest() throws Exception
  {
    setRequestAttribute("ContenutoES","U126");
    return PG_LOAD_RICERCA_ESECUZIONE_SS;  
  }
}