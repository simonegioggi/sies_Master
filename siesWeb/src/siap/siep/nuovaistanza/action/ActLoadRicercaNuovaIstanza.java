package siap.siep.nuovaistanza.action;


/**
* <p>Title: ActLoadRicercaNuovaIstanza</p>
* <p>Description: Classe Action per la load ricerca di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.Action;
import f3b.web.html.Option;

public class ActLoadRicercaNuovaIstanza extends Action implements ICostantiNuovaIstanza
{
 /*****************************************************************************
  * Azione di caricamento della pagina di ricerca. Si occupa di precaricare
  * tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {

    //=============================================================
    // Aggiungere qui eventuali caricamento di combo o altri dati 
    // da passare alla finestra di ricerca, e relative setRequest 
    //=============================================================
    // es: 
    //Option lOption = new Option(codice per caricare la option); 
    //setRequestAttribute("nome_attributo", "" + lOption); 

    Option lOption = null;
	try {
		lOption = new Option( DecodificheManager.getInstance().getStatoNuovaIstanza(), 45);
	} catch (Exception e) {
		
		e.printStackTrace();
	}
      setRequestAttribute("statonuovaistanza", "" + lOption );

    //restituisce la jsp di visualizzazione della pagina di ricerca 
    return PG_LOAD_RICERCANUOVAISTANZA;

  }
}