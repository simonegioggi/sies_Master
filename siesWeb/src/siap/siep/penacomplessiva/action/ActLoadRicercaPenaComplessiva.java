package siap.siep.penacomplessiva.action;

import siap.sico.web.ActionSiap;


/**
* <p>Title: ActLoadRicercaPenaComplessiva</p>
* <p>Description: Classe Action per la load ricerca di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva
{
	public String processRequest() throws Exception
  {

   return PG_LOAD_RICERCAPENACOMPLESSIVA;  //restituisce la jsp di VIEW
  }
}