package siap.siep.annotazionemanuale.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaAnnotazioneManuale</p>
* <p>Description: Classe Action per la load ricerca di AnnotazioneManuale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaAnnotazioneManuale extends ActionSiap implements ICostantiAnnotazioneManuale
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAANNOTAZIONEMANUALE;  //restituisce la jsp di VIEW 

	 }

}