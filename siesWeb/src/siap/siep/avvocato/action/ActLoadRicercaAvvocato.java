package siap.siep.avvocato.action;


/**
* <p>Title: ActLoadRicercaAvvocato</p>
* <p>Description: Classe Action per la load ricerca di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


import siap.sico.web.ActionSiap;


public class ActLoadRicercaAvvocato extends ActionSiap implements ICostantiAvvocato
{
  public String processRequest() throws Exception
  {
	
    String lPage =  PG_LOAD_RICERCAAVVOCATO;

    if (! isRequestParameterNullObj("modalita"))
    {
      if (getRequestStringParameter("modalita").compareTo("BREVE")==0)
        lPage = PG_LOAD_RICERCAAVVOCATOBREVE;
    }

    return lPage;  //restituisce la jsp di VIEW
  }
}