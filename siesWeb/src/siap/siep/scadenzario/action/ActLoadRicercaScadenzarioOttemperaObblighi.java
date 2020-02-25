package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadRicercaScadenzarioOttemperaObblighi</p>
 * <p>Description: Classe Action per la load ricerca di Scadenzario Pene Sospese</p>
 * <p> 		per quantoriguarda i termini di 'Ottemperanza Obblighi' </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadRicercaScadenzarioOttemperaObblighi extends ActionSiap implements ICostantiScadenzario
{
  public String processRequest() throws F3BException 
  {
    return PG_LOAD_RICERCASCADENZARIO_OTTEMPERAOBBLIGHI;  //restituisce la jsp di VIEW 
  }
}