package siap.sius.magistratorelatore.action;

/**
* <p>Title: ActLoadRicercaFSPInsMagistratoRelatore</p>
* <p>Description: Classe Action per la load di RicercaFSPInsMagistratoRelatore</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

public class ActLoadRicercaFSPInsMagistratoRelatore extends ActLoadRicercaFSPuntuale
implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    setRequestAttribute("nextAction", "siap.sius.magistratorelatore.action.ActLoadInserisciMagistratoRelatore" );
    setRequestAttribute("functionName", "Ricerca Provvedimenti per Inserimento Magistrato Relatore" );

    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}
