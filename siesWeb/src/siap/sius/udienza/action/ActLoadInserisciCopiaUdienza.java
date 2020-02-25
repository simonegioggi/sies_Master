package siap.sius.udienza.action;

import f3b.util.F3BException;

/**
* <p>Title: ActLoadInserisciCopiaUdienza</p>
* <p>Description: Classe Action per la load modifica Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciCopiaUdienza extends ActLoadModificaUdienza
{
  public String processRequest() throws F3BException
  {
    // valorizzazione della request
    preparaRequest();

    mUdiMod.setDataUdienza(null);
    // Imposta la risposta nella request.
    setRequestAttribute("modalita", "X");
    setRequestAttribute("udienza", mUdiMod);


    return PG_LOAD_INSERISCIUDIENZA;
  }

}