package siap.siep.avvocato.action;


/**
* <p>Title: ActLoadLiastaAvvocatoPopup</p>
* <p>Description: Classe Action per la load ricerca di Avvocato</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 5.0
*/


import siap.sico.web.ActionSiap;


public class ActLoadListaAvvocatoPopup extends ActionSiap implements ICostantiAvvocato
{
  public String processRequest() throws Exception
  {
	
    String lPage =  PG_LOAD_LISTA_AVVOCATO_POP_UP;

    return lPage;  //restituisce la jsp di VIEW
  }
}