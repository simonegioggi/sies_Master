package siap.sico.decodifiche.action;

import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadUffSorvPerDistretto extends ActionSiap implements ICostantiComune
{
  public String processRequest() throws F3BException
  {
    String lCodDistretto = getRequestStringParameter( ICostantiUfficio.CAMPO_COD_DISTRETTO) ;

    setRequestAttribute("codicedistretto", lCodDistretto );
    String codTipoUff=this.getRequestStringParameter("codTipoUff");
    this.setRequestAttribute("codTipoUff",codTipoUff);


    return PG_FILTRA_UFF_COMUNE; //restituisce la jsp di VIEW
  }
}
