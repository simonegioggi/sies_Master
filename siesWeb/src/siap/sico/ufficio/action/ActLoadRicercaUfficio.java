package siap.sico.ufficio.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaUfficio extends ActionSiap implements ICostantiUfficio
{
    public String processRequest() throws F3BException
    {
      return PG_LOAD_RICERCAUFFICIO; //restituisce la jsp di VIEW
    }
}
