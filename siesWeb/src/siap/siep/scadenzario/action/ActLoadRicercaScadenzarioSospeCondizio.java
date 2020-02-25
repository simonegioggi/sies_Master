package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadRicercaScadenzarioSospeCondizio</p>
 * <p>Description: Classe Action per la load ricerca di Scadenzario Pene Sospese</p>
 * <p> 		per quantoriguarda i termini di 'Sospensione Condizionale' </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 4.0
 */

public class ActLoadRicercaScadenzarioSospeCondizio extends ActionSiap implements ICostantiScadenzario
{
  public String processRequest() throws F3BException 
  {
    return PG_LOAD_RICERCASCADENZARIO_SOSPECONDIZIO;  //restituisce la jsp di VIEW 
  }
}