package siap.siep.penasospesa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadInserisciRicEstinzioneReato</p>
* <p>Description: Classe Action per la load inserisci di una Richiesta Estinzione Reato.</p> 
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: Agile</p>
* <p> @author: Luigi</p>
* @version 1.0
*/

public class ActLoadInsAnnotazioneAdempObblighi extends ActLoadInserisciRichiesta
{

	
  public String processRequest() throws Exception
  {
	  mNomeAction = "siap.siep.penasospesa.action.ActLoadInsAnnotazioneAdempObblighi";
	  mNomeJsp = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciAnnAdempObblighi.jsp"; //PG_LOAD_INSERISCIRICHIESTAREVOCA;
   
      Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
      setRequestAttribute("autoritaSentenza", ""+lOption );

      //Autorità esterna altra
	    Option lOptionAutoritaAltra = null;
	    lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());
	    setRequestAttribute("autoritaEsternaAltra", "" + lOptionAutoritaAltra);

    return super.processRequest();

  }
  
 


}