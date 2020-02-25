package siap.sige.fascicolo.action;


/**
 * <p>Title: ActLoadModificaResidenzaFascicoloSige</p>
 * <p>Description: Classe Action per la modifica della residenza fascicolo SIGE</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class ActLoadModificaResidenzaFascicoloSige extends ActLoadModificaDomicilioFascicoloSige
{
  public String processRequest() throws Exception
  {
    super.processRequest();

    return ICostantiFascicoloSige.PG_LOAD_INSERISCIRESIDENZAFASCICOLOSIGE;  //restituisce la jsp di VIEW
  }
}