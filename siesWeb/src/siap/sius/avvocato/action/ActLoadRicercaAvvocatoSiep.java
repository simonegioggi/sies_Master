package siap.sius.avvocato.action;


/**
* <p>Title: ActLoadRicercaAvvocato</p>
* <p>Description: Classe Action per la load ricerca di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaAvvocatoSiep extends ActionSiap implements ICostantiAvvocato
{
public String processRequest() throws F3BException
  {

  String lPage =  PG_LOAD_RICERCAAVVOCATOSIEP;


  if (! isRequestParameterNullObj("modalita"))
     {
       if (getRequestStringParameter("modalita").compareTo("BREVE")==0)
          lPage = PG_LOAD_RICERCAAVVOCATOBREVESIEP;
     }

    return lPage;  //restituisce la jsp di VIEW

}






}