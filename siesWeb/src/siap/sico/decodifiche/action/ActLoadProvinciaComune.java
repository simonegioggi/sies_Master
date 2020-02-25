package siap.sico.decodifiche.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadProvinciaComune extends ActionSiap implements ICostantiComune
{
  public String processRequest() throws F3BException
  {
    String lCodProvincia = getRequestStringParameter( CAMPO_COD_PROVINCIA) ;
    setRequestAttribute("provincia", lCodProvincia );
    return PG_COMUNEPROV; //restituisce la jsp di VIEW
  }
}
