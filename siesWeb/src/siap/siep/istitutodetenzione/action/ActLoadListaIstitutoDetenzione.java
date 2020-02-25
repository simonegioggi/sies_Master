package siap.siep.istitutodetenzione.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadListaIstitutoDetenzione extends ActionSiap implements ICostantiIstitutoDetenzione
{
  public String processRequest() throws F3BException
  {
    return PG_LISTA_ISTITUTO_FILTROTIPO; //restituisce la jsp di VIEW
   }
}
