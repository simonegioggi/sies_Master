package siap.sius.fascicolo.action;


/**
 * <p>Title: ActLoadModificaResidenzaFascicoloSius</p>
 * <p>Description: Classe Action per lamodifica della residenza fascicolo sius</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActLoadModificaResidenzaFascicoloSius extends ActLoadModificaDomicilioFascicoloSius
{
  public String processRequest() throws Exception
  {
    super.processRequest();

    return ICostantiFascicoloSius.PG_LOAD_INSERISCRESIDENZAFASCICOLOSIUS;  //restituisce la jsp di VIEW
  }
}