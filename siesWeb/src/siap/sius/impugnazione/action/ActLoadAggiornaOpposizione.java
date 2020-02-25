package siap.sius.impugnazione.action;


/**
* <p>Title: ActLoadModificaOpposizione</p>
* <p>Description: Classe Action per la load Modifica di Opposizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* @since 06/2014
*/
public class ActLoadAggiornaOpposizione extends ActLoadModificaOpposizione implements ICostantiImpugnazione
{
  public String processRequest() throws Exception
  {
    String lPage = null;
    lPage = super.processRequest();
    setRequestAttribute("modalita", "A");

    return lPage;
  }
}
