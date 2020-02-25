package siap.sico.decodifiche.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaComune extends ActionSiap implements ICostantiComune
{
    public String processRequest() throws F3BException
    {
      return PG_LOAD_RICERCACOMUNE; //restituisce la jsp di VIEW
    }
}
