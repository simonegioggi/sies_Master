package siap.siep.cumulo.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria</p>
* <p>Description: Classe Action per la load ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria extends ActionSiap implements ICostantiCumulo
{
 public String processRequest() throws F3BException

  {
    return PG_LOAD_INSERIMENTO_MISURA_SICUREZZA_PENA_ACCESSORIA;
  }

}