package siap.siep.reato.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaReatiFascicoloFramset extends ActionSiap implements ICostantiReato
{
    public String processRequest() throws F3BException
    {
      return PG_LOADRICERCAREATIFASCICOLOFRAMSET; //restituisce la jsp di VIEW
    }
}